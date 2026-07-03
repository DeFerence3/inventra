package com.deference.inventra.domain.model.approvals


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Step(
    @SerialName("id")
    val id: Int,
    @SerialName("instanceId")
    val instanceId: Int,
    @SerialName("stepOrder")
    val stepOrder: Int,
    @SerialName("approverId")
    val approverId: Int,
    @SerialName("approverName")
    val approverName: String,
    @SerialName("designationId")
    val designationId: Int,
    @SerialName("designationName")
    val designationName: String,
    @SerialName("departmentId")
    val departmentId: Int,
    @SerialName("departmentName")
    val departmentName: String?,
    @SerialName("email")
    val email: String,
    @SerialName("status")
    val status: String,
    @SerialName("isDelegatedUser")
    val isDelegatedUser: Boolean,
    @SerialName("actionAt")
    val actionAt: String?,
    @SerialName("remarks")
    val remarks: String?,
    @SerialName("isActive")
    val isActive: Boolean,
    @SerialName("createdUserId")
    val createdUserId: Int,
    @SerialName("createdUserName")
    val createdUserName: String,
    @SerialName("createdDate")
    val createdDate: String
)