package com.deference.inventra.domain.model.grn


import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GrnRequest(
    @SerialName("transNo")
    val transNo: String? = null,
    @SerialName("transDate")
    val transDate: String,
    @SerialName("locationId")
    val locationId: Int,
    @SerialName("locationName")
    val locationName: String? = null,
    @SerialName("vendorId")
    val vendorId: Int,
    @SerialName("vendorName")
    val vendorName: String? = null,
    @SerialName("vendorGroupName")
    val vendorGroupName: String? = null,
    @SerialName("poNumbers")
    val poNumbers: String? = null,
    @SerialName("poIDs")
    val poIDs: String? = null,
    @SerialName("deliveryChallanNo")
    val deliveryChallanNo: String,
    @SerialName("deliveryChallanDate")
    val deliveryChallanDate: LocalDateTime,
    @SerialName("invoiceNo")
    val invoiceNo: String? = null,
    @SerialName("invoiceDate")
    val invoiceDate: String? = null,
    @SerialName("deliveryDate")
    val deliveryDate: String? = null,
    @SerialName("netAmount")
    val netAmount: Double? = null,
    @SerialName("discountAmount")
    val discountAmount: Double,
    @SerialName("taxableAmount")
    val taxableAmount: Double,
    @SerialName("taxAmount")
    val taxAmount: Double? = null,
    @SerialName("grossAmount")
    val grossAmount: Double? = null,
    @SerialName("isInvoiced")
    val isInvoiced: Boolean,
    @SerialName("currencyID")
    val currencyID: Int? = null,
    @SerialName("currencyCode")
    val currencyCode: String? = null,
    @SerialName("baseCurrencyCode")
    val baseCurrencyCode: String? = null,
    @SerialName("convertionRateToBaseCurrency")
    val convertionRateToBaseCurrency: Double? = null,
    @SerialName("isCancelled")
    val isCancelled: Boolean = false,
    @SerialName("cancelledDate")
    val cancelledDate: String? = null,
    @SerialName("isDraft")
    val isDraft: Boolean = false,
    @SerialName("status")
    val status: Int = 1,
    @SerialName("items")
    val items: List<Item>
)