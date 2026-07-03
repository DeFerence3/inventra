package com.deference.inventra.presentation.purchaseOrder

sealed interface PurchaseOrderActions {
    data class OnSearchQueryChanged(val query: String) : PurchaseOrderActions
    data class OnTogglePO(val poUUID: String) : PurchaseOrderActions
    data object LoadNextPage : PurchaseOrderActions
    data object Refresh : PurchaseOrderActions
}
