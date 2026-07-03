package com.deference.inventra.data.remote

import com.deference.inventra.domain.model.Paginated
import com.deference.inventra.domain.model.auth.LoginRequest
import com.deference.inventra.domain.model.auth.LoginResponse
import com.deference.inventra.domain.model.master.Supplier
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("api/auth/account/login")
    fun onLogin(
        @Body signInRequest: LoginRequest
    ): Deferred<Response<LoginResponse>>

    @GET("api/master/supplier/paged")
    fun searchSupplier(
        @Query("name") name: String?,
        @Query("page") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Deferred<Response<Paginated<Supplier>>>
}