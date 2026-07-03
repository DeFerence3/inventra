package com.deference.inventra.presentation.orderItem

import com.deference.inventra.domain.model.purchase.OrderItem

data class OrderItemListState(
    val items: List<OrderItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
