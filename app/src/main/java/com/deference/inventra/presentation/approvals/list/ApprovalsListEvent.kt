package com.deference.inventra.presentation.approvals.list

sealed interface ApprovalsListEvent {
    data class Error(val message: String) : ApprovalsListEvent
    data object Success : ApprovalsListEvent
}
