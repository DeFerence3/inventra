package com.deference.inventra.data.remote

import com.deference.inventra.domain.model.spotcheck.SpotCheckRequestBody
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface StockApiService {
    @POST("api/stock/StockAdjustment")
    fun spotCheck(@Body body: SpotCheckRequestBody): Deferred<Response<Unit>>
}