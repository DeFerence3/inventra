package com.deference.inventra.presentation.core.components.selectors

data class SearchWrapper<T>(
    val header: String,
    val supportingText: String?,
    val item: T
)
