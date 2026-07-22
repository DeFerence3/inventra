package com.deference.inventra.domain.model.purchase.requisition


import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Header(
    @SerialName("transNo")
    val transNo: String,
    @SerialName("transDate")
    val transDate: LocalDateTime,
    @SerialName("expectedDate")
    val expectedDate: LocalDateTime,
    @SerialName("locationName")
    val locationName: String,
    @SerialName("internalNotes")
    val internalNotes: String?
)