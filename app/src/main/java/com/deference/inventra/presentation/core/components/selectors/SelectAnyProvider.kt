package com.deference.inventra.presentation.core.components.selectors

data class SelectAnyProvider(
    var title: String,
    var subTitle: String? = null,
    var item: Any?,
    var image: String? = null
) {
    companion object {
        fun filter(list: List<SelectAnyProvider>, filter: Filter): List<SelectAnyProvider> {
            return when (filter) {
                is Filter.ByQuery -> list.filter { it.title.contains(filter.query, ignoreCase = true) }
                Filter.None -> list
            }
        }
    }
}