package com.deference.inventra.domain.usecase

import com.deference.inventra.core.utils.network.ApiResponseHandler
import com.deference.inventra.core.utils.network.RequestState
import com.deference.inventra.core.utils.network.isInternetAvailable
import com.deference.inventra.domain.model.Paginated
import com.deference.inventra.domain.model.stock.StockReceiptResponseBody
import com.deference.inventra.domain.model.stock.StockReceiptStatus
import com.deference.inventra.domain.repository.SpotCheckRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.SerializationException
import java.io.IOException
import javax.inject.Inject

class GetStockReceiptsUseCase @Inject constructor(
    private val repository: SpotCheckRepo
) {
    operator fun invoke(
        status: StockReceiptStatus,
        page: Int,
        pageSize: Int
    ): Flow<RequestState<Paginated<StockReceiptResponseBody>>> = flow {
        if (!isInternetAvailable()) {
            emit(RequestState.Error("No internet connection"))
            return@flow
        }

        try {
            emit(RequestState.Loading)
            val deferredResponse = repository.listStockRequest(status, page, pageSize)
            val retrofitResponse = deferredResponse.await()
            if (retrofitResponse.isSuccessful && retrofitResponse.body() != null) {
                emit(RequestState.Success(retrofitResponse.body()!!))
            } else {
                emit(ApiResponseHandler.getApiErrors(retrofitResponse))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            emit(RequestState.Error("Network request failed: ${e.message}"))
        } catch (e: SerializationException) {
            e.printStackTrace()
            emit(RequestState.Error("Invalid response from server"))
        } catch (_: TimeoutCancellationException) {
            emit(RequestState.Error("Request timed out"))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RequestState.Error("Unhandled Exception: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)
}
