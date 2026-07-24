package com.deference.inventra.domain.model.stock


import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockReceiptRequest(
    @SerialName("transNo")
    val transNo: String,
    @SerialName("transDate")
    val transDate: LocalDateTime,
    @SerialName("issueLocationId")
    val issueLocationId: Int,
    @SerialName("issueLocationName")
    val issueLocationName: String,
    @SerialName("receiptLocationId")
    val receiptLocationId: Int,
    @SerialName("receiptLocationName")
    val receiptLocationName: String,
    @SerialName("transferNos")
    val transferNos: String,
    @SerialName("transferIDs")
    val transferIDs: String,
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
    val stockReceiptRequestItems: List<StockReceiptRequestItem>
)