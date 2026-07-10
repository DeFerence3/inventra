package com.deference.inventra.presentation.core.components.selectors.components

sealed class Filter {
    data class ByQuery(val query: String) : Filter()
    data object None : Filter()
}

sealed class Sort {
    data object ByName : Sort()
    data object None : Sort()
}

sealed class Group {
    data object ByFirstLetter : Group()
    data object None : Group()
}
