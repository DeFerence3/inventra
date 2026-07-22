package com.deference.inventra.domain.model.purchase.requisition


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequisitionSummaryResponse(
    @SerialName("header")
    val header: Header,
    @SerialName("items")
    val requistedItems: List<RequestedItem>
)