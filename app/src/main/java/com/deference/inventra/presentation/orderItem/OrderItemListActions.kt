package com.deference.inventra.presentation.orderItem

import kotlinx.datetime.LocalDateTime

sealed interface OrderItemListActions {
    data class SaveGrn(
        val deliveryChallanNo: String,
        val deliveryChallanDate: LocalDateTime
    ) : OrderItemListActions
    data object DismissError : OrderItemListActions
    data class UpdateItemAmounts(
        val purchaseOrderItemUuid: String,
        val qty: Double,
        val rate: Double,
    ) : OrderItemListActions
}

