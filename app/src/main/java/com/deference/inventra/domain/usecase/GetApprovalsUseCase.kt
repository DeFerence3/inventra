package com.deference.inventra.domain.usecase

import com.deference.inventra.core.utils.network.ApiResponseHandler
import com.deference.inventra.core.utils.network.RequestState
import com.deference.inventra.core.utils.network.isInternetAvailable
import com.deference.inventra.domain.model.Paginated
import com.deference.inventra.domain.model.approvals.ApprovalItem
import com.deference.inventra.domain.repository.PurchaseRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class GetApprovalsUseCase @Inject constructor(
    private val repository: PurchaseRepo
) {
    operator fun invoke(
        status: String,
        transType: String,
        search: String?,
        isGrouped: Boolean = true,
        page: Int = 1,
        pageSize: Int = 20
    ): Flow<RequestState<Paginated<ApprovalItem>>> = flow {
        if (!isInternetAvailable()) {
            emit(RequestState.Error("No internet connection"))
            return@flow
        }

        try {
            emit(RequestState.Loading)
            val deferredResponse = repository.getApprovals(status,transType, search,isGrouped, page, pageSize)
            val retrofitResponse = deferredResponse.await()
            if (retrofitResponse.isSuccessful) {
                val response = retrofitResponse.body()
                if (response != null) {
                    emit(RequestState.Success(response))
                } else {
                    emit(RequestState.Error("Received empty response"))
                }
            } else {
                emit(ApiResponseHandler.getApiErrors(retrofitResponse))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            emit(RequestState.Error("Network request failed: ${e.message}"))
        } catch (e: TimeoutCancellationException) {
            emit(RequestState.Error("Request timed out"))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RequestState.Error("Unhandled Exception: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)
}
