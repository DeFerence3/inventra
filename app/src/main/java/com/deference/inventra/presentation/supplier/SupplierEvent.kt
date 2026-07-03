package com.deference.inventra.presentation.supplier

sealed interface SupplierEvent {
    data class Error(val message: String) : SupplierEvent
}
