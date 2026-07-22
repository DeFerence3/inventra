package com.deference.inventra.data.repository

import com.deference.inventra.data.remote.StockApiService
import com.deference.inventra.domain.model.spotcheck.SpotCheckRequestBody
import com.deference.inventra.domain.model.stock.StockRequestRequestBody
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
}
