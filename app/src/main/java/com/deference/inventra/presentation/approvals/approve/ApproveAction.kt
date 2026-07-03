package com.deference.inventra.presentation.approvals.approve

sealed interface ApproveAction {
    data object OnDismissDetails : ApproveAction
    data object OnDismissError : ApproveAction
    data object Refresh : ApproveAction
    data class OnSearchQueryChanged(val query: String) : ApproveAction
    data class OnCommentChange(val comment: String) : ApproveAction
    data class OnApprove(val approvalId: String) : ApproveAction
    data class OnReject(val approvalId: String) : ApproveAction
}
