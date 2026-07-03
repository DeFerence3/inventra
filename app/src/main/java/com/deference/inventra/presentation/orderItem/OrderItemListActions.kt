package com.deference.inventra.presentation.orderItem

sealed interface OrderItemListActions {
    data object SaveGrn : OrderItemListActions
    data object DismissError : OrderItemListActions
}
