package com.deference.inventra.presentation.purchaserequest

sealed interface PurchaseRequestEvent {
    data class Error(val message: String) : PurchaseRequestEvent
    data object Success : PurchaseRequestEvent
}
