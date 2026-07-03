package com.deference.inventra.domain.model.auth


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("token")
    val token: String,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("userId")
    val userId: Int,
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: String,
    @SerialName("role")
    val role: String? = null
)