package com.deference.inventra.presentation.stockrequest

sealed interface StockRequestEvent {
    data class Error(val message: String) : StockRequestEvent
    data object Success : StockRequestEvent
}
