package com.deference.inventra.presentation.stockreceipt

sealed interface StockReceiptListEvent {
    data class Error(val message: String) : StockReceiptListEvent
}
