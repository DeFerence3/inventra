package com.deference.inventra.domain.repository

import com.deference.inventra.domain.model.Paginated
import com.deference.inventra.domain.model.spotcheck.SpotCheckRequestBody
import com.deference.inventra.domain.model.stock.StockRequestRequestBody
import com.deference.inventra.domain.model.stock.StockReceiptRequest
import com.deference.inventra.domain.model.stock.StockReceiptStatus
import com.deference.inventra.domain.model.stock.StockReceiptResponseBody
import com.deference.inventra.domain.model.stock.receipt.ReceiptResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface SpotCheckRepo {
    fun submitSpotCheck(body: SpotCheckRequestBody): Deferred<Response<Unit>>
    fun getNextSerialNo(body: Map<String, String>): Deferred<Response<com.deference.inventra.domain.model.serialno.SerialNo>>
    fun saveStockRequest(body: StockRequestRequestBody): Deferred<Response<Unit>>
    fun saveStockReceipt(body: StockReceiptRequest): Deferred<Response<Unit>>
    fun listStockRequest(
        status: StockReceiptStatus,
        page: Int,
        pageSize: Int
    ): Deferred<Response<Paginated<StockReceiptResponseBody>>>
    fun getStockReceiptDetails(transNo: String): Deferred<Response<ReceiptResponse>>
}
