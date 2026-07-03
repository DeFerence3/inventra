package com.deference.inventra.domain.model.purchase

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseOrder(
    @SerialName("id")
    val id: Int,
    @SerialName("uuid")
    val uuid: String,
    @SerialName("transNo")
    val transNo: String,
    @SerialName("transDate")
    val transDate: String,
    @SerialName("locationId")
    val locationId: Int,
    @SerialName("locationName")
    val locationName: String,
    @SerialName("vendorId")
    val vendorId: Int,
    @SerialName("vendorName")
    val vendorName: String,
    @SerialName("vendorGroupId")
    val vendorGroupId: Int,
    @SerialName("vendorGroupName")
    val vendorGroupName: String
)