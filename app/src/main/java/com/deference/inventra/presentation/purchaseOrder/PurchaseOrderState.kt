package com.deference.inventra.presentation.purchaseOrder

import com.deference.inventra.domain.model.purchase.PurchaseOrder

data class PurchaseOrderState(
    val supplierId: Int = 0,
    val searchQuery: String = "",
    val purchaseOrders: List<PurchaseOrder> = emptyList(),
    val selectedPOUUIDs: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val page: Int = 1,
    val pageSize: Int = 20,
    val isLastPage: Boolean = false
)
