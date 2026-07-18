package com.deference.inventra.presentation.core.components.selectors

data class SelectAnyProvider(
    var title: String,
    var subTitle: String? = null,
    var item: Any?,
    var image: String? = null
)