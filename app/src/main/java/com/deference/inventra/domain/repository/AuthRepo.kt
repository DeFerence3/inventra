package com.deference.inventra.domain.repository

import com.deference.inventra.domain.model.auth.LoginResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface AuthRepo {
    suspend fun login(username: String, password: String): Deferred<Response<LoginResponse>>
}