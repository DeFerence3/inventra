package com.deference.inventra.presentation.core.components.selectors

data class SingleSelectionState(
    val list: List<SelectAnyProvider> = emptyList(),
    val searchQuery: String = "",
    val type: String = "",
    val isLoading: Boolean = false
)
