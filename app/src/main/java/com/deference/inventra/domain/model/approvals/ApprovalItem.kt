package com.deference.inventra.domain.model.approvals


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApprovalItem(
    @SerialName("uuid")
    val uuid: String,
    @SerialName("transUuid")
    val transUuid: String,
    @SerialName("transNo")
    val transNo: String,
    @SerialName("transDate")
    val transDate: String,
    @SerialName("transType")
    val transType: ApprovalRequestType,
    @SerialName("itemName")
    val itemName: String,
    @SerialName("amount")
    val amount: Double,
    @SerialName("approvalFlow")
    val approvalFlow: String,
    @SerialName("dataEndPoint")
    val dataEndPoint: String,
    @SerialName("locationName")
    val locationName: String,
    @SerialName("status")
    val status: String,
    @SerialName("statuses")
    val statuses: List<String?>,
    @SerialName("requestedUserName")
    val requestedUserName: String,
    @SerialName("lastActionUserName")
    val lastActionUserName: String?,
    @SerialName("nextActionUserName")
    val nextActionUserName: String?
)