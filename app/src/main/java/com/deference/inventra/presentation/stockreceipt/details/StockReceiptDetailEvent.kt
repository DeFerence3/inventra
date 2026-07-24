package com.deference.inventra.presentation.stockreceipt.details

sealed interface StockReceiptDetailEvent {
    data class Error(val message: String) : StockReceiptDetailEvent
    data object Success : StockReceiptDetailEvent
}