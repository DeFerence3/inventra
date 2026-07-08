package com.deference.inventra.domain.model.spotcheck


import com.deference.inventra.domain.model.purchase.Item
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpotCheckRequestBody(
    @SerialName("transNo")
    val transNo: String,
    @SerialName("transDate")
    val transDate: LocalDateTime,
    @SerialName("locationId")
    val locationId: Int,
    @SerialName("remarks")
    val remarks: String,
    @SerialName("isCancelled")
    val isCancelled: Boolean,
    @SerialName("isDraft")
    val isDraft: Boolean,
    @SerialName("status")
    val status: Int,
    @SerialName("currencyCode")
    val currencyCode: String,
    @SerialName("baseCurrencyCode")
    val baseCurrencyCode: String,
    @SerialName("currencyConversionRate")
    val currencyConversionRate: Int,
    @SerialName("items")
    val items: List<Item>
)