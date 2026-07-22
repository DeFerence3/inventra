package com.deference.inventra.domain.model.stock

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockRequestRequestBody(
    @SerialName("transNo")
    val transNo: String,
    @SerialName("transDate")
    val transDate: LocalDateTime,
    @SerialName("requestFromLocationId")
    val requestFromLocationId: Int,
    @SerialName("requestFromLocationName")
    val requestFromLocationName: String,
    @SerialName("requestToLocationId")
    val requestToLocationId: Int,
    @SerialName("requestToLocationName")
    val requestToLocationName: String,
    @SerialName("remarks")
    val remarks: String,
    @SerialName("status")
    val status: Int,
    @SerialName("totalNetAmount")
    val totalNetAmount: Double,
    @SerialName("totalVatAmount")
    val totalVatAmount: Double,
    @SerialName("totalGrossAmount")
    val totalGrossAmount: Double,
    @SerialName("items")
    val stockRequestItems: List<StockRequestItem>
)