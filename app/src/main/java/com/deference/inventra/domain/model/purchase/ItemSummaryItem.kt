package com.deference.inventra.domain.model.purchase


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemSummaryItem(
    @SerialName("items")
    val items: List<Item>
)