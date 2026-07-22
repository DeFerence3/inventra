package com.deference.inventra.domain.repository

import com.deference.inventra.domain.model.spotcheck.SpotCheckRequestBody
import com.deference.inventra.domain.model.stock.StockRequestRequestBody
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface SpotCheckRepo {
    fun submitSpotCheck(body: SpotCheckRequestBody): Deferred<Response<Unit>>
    fun getNextSerialNo(body: Map<String, String>): Deferred<Response<com.deference.inventra.domain.model.serialno.SerialNo>>
    fun saveStockRequest(body: StockRequestRequestBody): Deferred<Response<Unit>>
}
