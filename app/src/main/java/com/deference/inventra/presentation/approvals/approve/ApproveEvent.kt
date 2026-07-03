package com.deference.inventra.presentation.approvals.approve

sealed interface ApproveEvent {
    data class Error(val message: String) : ApproveEvent
    data object Success : ApproveEvent
}
