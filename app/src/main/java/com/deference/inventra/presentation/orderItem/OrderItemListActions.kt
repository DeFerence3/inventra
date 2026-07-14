package com.deference.inventra.presentation.orderItem

import com.deference.inventra.domain.model.item.SearchItem
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
    data class SearchItems(val query: String) : OrderItemListActions
    data class AddManualItem(
        val item: SearchItem,
        val qty: Double,
        val rate: Double
    ) : OrderItemListActions
}

