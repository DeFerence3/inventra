package com.deference.inventra.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Paginated<T>(
    @SerialName("items")
    val items: List<T>,
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("page")
    val page: Int,
    @SerialName("pageSize")
    val pageSize: Int,
    @SerialName("totalPages")
    val totalPages: Int
)