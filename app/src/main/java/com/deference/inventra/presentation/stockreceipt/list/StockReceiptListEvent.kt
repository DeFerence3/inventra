package com.deference.inventra.presentation.stockreceipt.list

sealed interface StockReceiptListEvent {
    data class Error(val message: String) : StockReceiptListEvent
}
