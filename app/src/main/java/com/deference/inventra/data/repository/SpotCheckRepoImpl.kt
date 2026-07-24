package com.deference.inventra.data.repository

import com.deference.inventra.data.remote.StockApiService
import com.deference.inventra.domain.model.Paginated
import com.deference.inventra.domain.model.spotcheck.SpotCheckRequestBody
import com.deference.inventra.domain.model.stock.StockReceiptRequest
import com.deference.inventra.domain.model.stock.StockReceiptResponseBody
import com.deference.inventra.domain.model.stock.StockReceiptStatus
import com.deference.inventra.domain.model.stock.StockRequestRequestBody
import com.deference.inventra.domain.model.stock.receipt.ReceiptResponse
import com.deference.inventra.domain.repository.SpotCheckRepo
import kotlinx.coroutines.Deferred
import retrofit2.Response
import javax.inject.Inject

class SpotCheckRepoImpl @Inject constructor(
    private val apiService: StockApiService,
    private val configApiService: com.deference.inventra.data.remote.ConfigApiService
) : SpotCheckRepo {
    override fun submitSpotCheck(body: SpotCheckRequestBody): Deferred<Response<Unit>> = apiService.spotCheck(body)

    override fun getNextSerialNo(body: Map<String, String>): Deferred<Response<com.deference.inventra.domain.model.serialno.SerialNo>> {
        return configApiService.getNextSerialNo(body)
    }

    override fun saveStockRequest(body: StockRequestRequestBody): Deferred<Response<Unit>> {
        return apiService.saveStockRequest(body)
    }

    override fun saveStockReceipt(body: StockReceiptRequest): Deferred<Response<Unit>> {
        return apiService.saveStockReceipt(body)
    }

    override fun listStockRequest(
        status: StockReceiptStatus,
        page: Int,
        pageSize: Int
    ): Deferred<Response<Paginated<StockReceiptResponseBody>>> {
        return apiService.listStockRequest(status.ordinal+1, page, pageSize)
    }

    override fun getStockReceiptDetails(transNo: String): Deferred<Response<ReceiptResponse>> {
        return apiService.details(transNo)
    }
}
