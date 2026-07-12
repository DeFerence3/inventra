package com.deference.inventra.domain.model.pr


import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrRequestBody(
    @SerialName("transNo")
    val transNo: String,
    @SerialName("transDate")
    val transDate: String,
    @SerialName("locationId")
    val locationId: Int,
    @SerialName("locationName")
    val locationName: String,
    @SerialName("expectedDate")
    val expectedDate: LocalDateTime,
    @SerialName("internalNotes")
    val internalNotes: String,
    @SerialName("status")
    val status: Int,
    @SerialName("currencyCode")
    val currencyCode: String,
    @SerialName("baseCurrencyCode")
    val baseCurrencyCode: String,
    @SerialName("currencyConversionRate")
    val currencyConversionRate: Int,
    @SerialName("userLevel")
    val userLevel: Int,
    @SerialName("items")
    val items: List<PrItem>
)