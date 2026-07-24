package com.deference.inventra.presentation.stockreceipt

import com.deference.inventra.domain.model.stock.receipt.ReceiptResponse

data class StockReceiptDetailState(
    val receipt: ReceiptResponse? = null,
    val isLoading: Boolean = false,
    val isSubmitted: Boolean = false,
    val error: String? = null
)
