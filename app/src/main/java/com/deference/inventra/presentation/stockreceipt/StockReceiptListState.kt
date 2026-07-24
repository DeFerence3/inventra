package com.deference.inventra.presentation.stockreceipt

import com.deference.inventra.domain.model.stock.StockReceiptResponseBody
import com.deference.inventra.domain.model.stock.StockReceiptStatus

data class StockReceiptListState(
    val receipts: List<StockReceiptResponseBody> = emptyList(),
    val selectedStatus: StockReceiptStatus = StockReceiptStatus.TRANSFERRED,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)
