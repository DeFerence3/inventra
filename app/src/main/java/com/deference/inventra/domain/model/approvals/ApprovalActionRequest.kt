package com.deference.inventra.domain.model.approvals

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApprovalActionRequest(
    @SerialName("instanceId")
    val instanceId: Int,
    @SerialName("instanceStepId")
    val instanceStepId: Int,
    @SerialName("transUuid")
    val transUuid: String,
    @SerialName("transType")
    val transType: String,
    @SerialName("comment")
    val comment: String
)