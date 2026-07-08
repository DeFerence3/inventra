package com.deference.inventra.presentation.spotcheck

sealed interface SpotCheckEvent {
    data class Error(val message: String) : SpotCheckEvent
    data object Success : SpotCheckEvent
}
