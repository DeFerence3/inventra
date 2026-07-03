package com.deference.inventra.domain.usecase

import com.deference.inventra.core.utils.network.ApiResponseHandler
import com.deference.inventra.core.utils.network.RequestState
import com.deference.inventra.core.utils.network.isInternetAvailable
import com.deference.inventra.domain.model.approvals.ApprovalActionRequest
import com.deference.inventra.domain.repository.PurchaseRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class PerformApprovalActionUseCase @Inject constructor(
    private val repository: PurchaseRepo
) {
    operator fun invoke(action: ApprovalActionRequest,status: String): Flow<RequestState<Unit>> = flow {
        if (!isInternetAvailable()) {
            emit(RequestState.Error("No internet connection"))
            return@flow
        }

        try {
            emit(RequestState.Loading)
            val deferredResponse = repository.performApprovalAction(action, status)
            val retrofitResponse = deferredResponse.await()
            if (retrofitResponse.isSuccessful) {
                emit(RequestState.Success(Unit))
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
