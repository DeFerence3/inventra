package com.deference.inventra.presentation.supplier

sealed interface SupplierActions {
    data class OnSearchQueryChanged(val query: String) : SupplierActions
    data object LoadSuppliers : SupplierActions
    data object LoadNextPage : SupplierActions
    data object Refresh : SupplierActions
}
