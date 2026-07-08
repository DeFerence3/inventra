package com.deference.inventra.domain.model.serialno


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SerialNo(
    @SerialName("proposedSerial")
    val proposedSerial: String,
    @SerialName("nextSequence")
    val nextSequence: Int,
    @SerialName("patternUsed")
    val patternUsed: String
)