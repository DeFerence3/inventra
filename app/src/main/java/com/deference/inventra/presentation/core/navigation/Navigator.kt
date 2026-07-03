package com.deference.inventra.presentation.core.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class Navigator(startDestination: InventraRoutes) {
    val backStack: SnapshotStateList<InventraRoutes> = mutableStateListOf(startDestination)

    fun push(destination: InventraRoutes) {
        backStack.add(destination)
    }

    fun pop() {
        backStack.removeLastOrNull()
    }
}