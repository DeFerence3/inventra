package com.deference.inventra.data.repository

import com.deference.inventra.data.remote.ApiService
import com.deference.inventra.domain.model.auth.LoginRequest
import com.deference.inventra.domain.model.auth.LoginResponse
import com.deference.inventra.domain.repository.AuthRepo
import kotlinx.coroutines.Deferred
import retrofit2.Response

class AuthRepoImpl(
    private val apiService: ApiService
): AuthRepo {
    override suspend fun login(
        username: String,
        password: String,
    ): Deferred<Response<LoginResponse>> {
        val body = LoginRequest(username, password)
        return apiService.onLogin(body)
    }
}