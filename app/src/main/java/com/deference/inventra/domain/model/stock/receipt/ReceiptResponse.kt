package com.deference.inventra.domain.model.stock.receipt


import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReceiptResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("uuid")
    val uuid: String,
    @SerialName("transNo")
    val transNo: String,
    @SerialName("transDate")
    val transDate: LocalDateTime,
    @SerialName("issueFromLocationId")
    val issueFromLocationId: Int,
    @SerialName("issueFromLocationName")
    val issueFromLocationName: String,
    @SerialName("receiptToLocationId")
    val receiptToLocationId: Int,
    @SerialName("receiptToLocationName")
    val receiptToLocationName: String,
    @SerialName("requestNos")
    val requestNos: String,
    @SerialName("requestedIDs")
    val requestedIDs: String,
    @SerialName("isAutoReceipt")
    val isAutoReceipt: Boolean,
    @SerialName("remarks")
    val remarks: String,
    @SerialName("isProcessed")
    val isProcessed: Boolean,
    @SerialName("isCancelled")
    val isCancelled: Boolean,
    @SerialName("cancelledDate")
    val cancelledDate: LocalDateTime?,
    @SerialName("isDraft")
    val isDraft: Boolean,
    @SerialName("status")
    val status: Int,
    @SerialName("tenantId")
    val tenantId: Int,
    @SerialName("createdUserId")
    val createdUserId: Int,
    @SerialName("createdUserName")
    val createdUserName: String,
    @SerialName("createdDate")
    val createdDate: LocalDateTime,
    @SerialName("updatedUserId")
    val updatedUserId: Int,
    @SerialName("updatedUserName")
    val updatedUserName: String,
    @SerialName("updatedDate")
    val updatedDate: LocalDateTime,
    @SerialName("items")
    val items: List<Item>
)