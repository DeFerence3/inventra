package com.deference.inventra.domain.usecase

import com.deference.inventra.core.utils.network.ApiResponseHandler
import com.deference.inventra.core.utils.network.RequestState
import com.deference.inventra.core.utils.network.isInternetAvailable
import com.deference.inventra.domain.model.approvals.ApprovalRequestType
import com.deference.inventra.domain.model.purchase.ItemSummaryItem
import com.deference.inventra.domain.repository.PurchaseRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.SerializationException
import java.io.IOException
import javax.inject.Inject

class GetApprovalItemsUseCase @Inject constructor(
    private val purchaseRepo: PurchaseRepo
) {
    operator fun invoke(
        type: ApprovalRequestType,
        poUUIDs: List<String>
    ): Flow<RequestState<List<ItemSummaryItem>>> = flow {

        if (!isInternetAvailable()) {
            emit(RequestState.Error("No internet connection"))
            return@flow
        }

        try {
            emit(RequestState.Loading)
            val deferredResponse = purchaseRepo.getPurchaseOrderItemsSummary(poUUIDs)
            val purchaseRequisitionResponse = purchaseRepo.getPurchaseRequisitionSummary(poUUIDs)
            when(type){
                ApprovalRequestType.ALL -> emit(RequestState.Error("Invalid type"))
                ApprovalRequestType.PURCHASE_REQUISITION -> {
                    val retrofitResponse = purchaseRequisitionResponse.await()
                    if (retrofitResponse.isSuccessful) {
                        val response = retrofitResponse.body()
                        if (response != null) {
                            val summaryItem = response.flatMap { it.requistedItems }.map { it.toSummaryItem() }
                            emit(RequestState.Success(listOf(ItemSummaryItem(summaryItem))))
                        } else {
                            emit(RequestState.Error("Received empty response"))
                        }
                    } else {
                        emit(ApiResponseHandler.getApiErrors(retrofitResponse))
                    }
                }
                ApprovalRequestType.PURCHASE_ORDER_ITEM -> {
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
                }
                ApprovalRequestType.STOCK_REQUEST -> {
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
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            emit(RequestState.Error("Network request failed: ${e.message}"))
        } catch (_: TimeoutCancellationException) {
            emit(RequestState.Error("Request timed out"))
        } catch (e: SerializationException) {
            e.printStackTrace()
            emit(RequestState.Error("Invalid response from server"))
        }catch (e: Exception) {
            e.printStackTrace()
            emit(RequestState.Error("Unhandled Exception: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)
}
