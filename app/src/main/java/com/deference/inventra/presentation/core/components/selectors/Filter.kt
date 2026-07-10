package com.deference.inventra.presentation.core.components.selectors

sealed class Filter {
    data class ByQuery(val query: String) : Filter()
    data object None : Filter()
}