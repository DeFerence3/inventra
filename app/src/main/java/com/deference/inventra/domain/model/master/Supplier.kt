package com.deference.inventra.domain.model.master

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Supplier(
    @SerialName("id")
    val id: Int,
    @SerialName("uuid")
    val uuid: String,
    @SerialName("name")
    val name: String,
    @SerialName("code")
    val code: String? = null,
    @SerialName("email")
    val email: String? = null,
)