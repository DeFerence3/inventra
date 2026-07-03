package com.deference.inventra.core.utils.network

import android.content.Context
import com.deference.inventra.core.Session
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(
    private val context: Context
): Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .method(originalRequest.method, originalRequest.body)
            .addHeader("Content-Type", "application/json")
            .addHeader("User-Agent", getUserAgent(context))
            .addHeader("Authorization", "Bearer " + Session.bearer)

        val response = chain.proceed(requestBuilder.build())

        if (response.code == 401 && Session.isLoggedIn) {
            Session.logout()
        }

        return response
    }
}