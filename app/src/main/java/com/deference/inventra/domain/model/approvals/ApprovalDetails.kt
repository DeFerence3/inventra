package com.deference.inventra.domain.model.approvals


import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApprovalDetails(
    @SerialName("uuid")
    val uuid: String,
    @SerialName("approvalDefinitionId")
    val approvalDefinitionId: Int,
    @SerialName("transUuid")
    val transUuid: String,
    @SerialName("transNo")
    val transNo: String,
    @SerialName("transDate")
    val transDate: LocalDateTime,
    @SerialName("transType")
    val transType: String,
    @SerialName("itemName")
    val itemName: String,
    @SerialName("amount")
    val amount: Double?,
    @SerialName("approvalFlow")
    val approvalFlow: String,
    @SerialName("dataEndPoint")
    val dataEndPoint: String,
    @SerialName("approvalLevel")
    val approvalLevel: Int,
    @SerialName("status")
    val status: String,
    @SerialName("remarks")
    val remarks: String,
    @SerialName("currentStep")
    val currentStep: Int,
    @SerialName("lastActionUserId")
    val lastActionUserId: Int?,
    @SerialName("lastActionUserName")
    val lastActionUserName: String?,
    @SerialName("nextActionUserId")
    val nextActionUserId: Int?,
    @SerialName("nextActionUserName")
    val nextActionUserName: String?,
    @SerialName("requestedUserId")
    val requestedUserId: Int,
    @SerialName("requestedUserName")
    val requestedUserName: String,
    @SerialName("requestedDate")
    val requestedDate: String,
    @SerialName("lastActionDate")
    val lastActionDate: String?,
    @SerialName("entityId")
    val entityId: Int,
    @SerialName("isActive")
    val isActive: Boolean,
    @SerialName("createdUserId")
    val createdUserId: Int,
    @SerialName("createdUserName")
    val createdUserName: String,
    @SerialName("createdDate")
    val createdDate: String,
    @SerialName("updatedUserId")
    val updatedUserId: Int,
    @SerialName("updatedUserName")
    val updatedUserName: String,
    @SerialName("updatedDate")
    val updatedDate: String,
    @SerialName("steps")
    val steps: List<Step>
)