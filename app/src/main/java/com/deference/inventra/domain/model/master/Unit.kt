package com.deference.inventra.domain.model.master


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Unit(
    @SerialName("id")
    val id: Int,
    @SerialName("uuid")
    val uuid: String,
    @SerialName("name")
    val name: String,
    @SerialName("baseUnitName")
    val baseUnitName: String,
    @SerialName("qtyInBaseUnit")
    val qtyInBaseUnit: Double,
    @SerialName("baseUnitId")
    val baseUnitId: Int,
    @SerialName("useUnitInStockTaking")
    val useUnitInStockTaking: Boolean,
    @SerialName("isBaseUnit")
    val isBaseUnit: Boolean,
    @SerialName("isPackUnit")
    val isPackUnit: Boolean,
    @SerialName("isActive")
    val isActive: Boolean,
    @SerialName("tenantId")
    val tenantId: Int,
    @SerialName("createdUserId")
    val createdUserId: Int,
    @SerialName("createdUserName")
    val createdUserName: String,
    @SerialName("updatedUserId")
    val updatedUserId: Int,
    @SerialName("updatedUserName")
    val updatedUserName: String
)