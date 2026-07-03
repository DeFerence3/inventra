package com.deference.inventra.presentation.purchaseOrder

sealed interface PurchaseOrderEvent {
    data class Error(val message: String) : PurchaseOrderEvent
}
