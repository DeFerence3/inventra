package com.deference.inventra.presentation.home

import androidx.compose.ui.graphics.painter.Painter
import com.deference.inventra.presentation.core.navigation.InventraRoutes

data class MenuItem(
    val title: String,
    val icon: Painter,
    val route: InventraRoutes? = null
)