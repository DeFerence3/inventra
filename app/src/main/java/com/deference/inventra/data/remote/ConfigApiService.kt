package com.deference.inventra.data.remote

import com.deference.inventra.domain.model.serialno.SerialNo
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ConfigApiService {
    @POST("api/config/SerialNumber/preview")
    fun getNextSerialNo(@Body transactionType: Map<String, String>): Deferred<Response<SerialNo>>
}