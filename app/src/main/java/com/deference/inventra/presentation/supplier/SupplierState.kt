package com.deference.inventra.presentation.supplier

import com.deference.inventra.domain.model.master.Supplier

data class SupplierState(
    val searchQuery: String = "",
    val suppliers: List<Supplier> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val page: Int = 1,
    val pageSize: Int = 20,
    val isLastPage: Boolean = false
)
