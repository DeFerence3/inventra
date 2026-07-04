package com.deference.inventra.presentation.orderItem

sealed interface OrderItemListActions {
    data object SaveGrn : OrderItemListActions
    data object DismissError : OrderItemListActions
    data class UpdateItemAmounts(
        val purchaseOrderItemUuid: String,
        val qty: Double,
        val rate: Double,
    ) : OrderItemListActions
}

