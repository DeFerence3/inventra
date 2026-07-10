package com.deference.inventra.domain.model.master

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serializable as JavaSerializable

@Serializable
data class Location(
    @SerialName("id")
    val id: Int,
    @SerialName("uuid")
    val uuid: String,
    @SerialName("name")
    val name: String,
    @SerialName("number")
    val number: String,
    @SerialName("code")
    val code: String,
    @SerialName("entityId")
    val entityId: Int
) : JavaSerializable
