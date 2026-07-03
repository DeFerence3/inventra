package com.deference.inventra.presentation.orderItem

sealed interface OrderItemListEvent {
    data class Error(val message: String) : OrderItemListEvent
    data object Success : OrderItemListEvent
}
