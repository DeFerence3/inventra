package com.deference.inventra.presentation.stockreceipt

import com.deference.inventra.domain.model.stock.StockReceiptStatus

sealed interface StockReceiptListActions {
    data class OnStatusFilterChange(val status: StockReceiptStatus) : StockReceiptListActions
    data class OnSearchQueryChanged(val query: String) : StockReceiptListActions
    data object Refresh : StockReceiptListActions
}
