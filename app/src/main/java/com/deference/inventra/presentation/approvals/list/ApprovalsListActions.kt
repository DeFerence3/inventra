package com.deference.inventra.presentation.approvals.list

import com.deference.inventra.domain.model.approvals.ApprovalRequestType

sealed interface ApprovalsListActions {
    data object OnDismissDetails : ApprovalsListActions
    data object Refresh : ApprovalsListActions
    data class OnStatusFilterChange(val status: String) : ApprovalsListActions
    data class OnTransTypeFilterChange(val transType: ApprovalRequestType) : ApprovalsListActions
    data class OnApprovalClick(val approvalId: String) : ApprovalsListActions
    data class OnSearchQueryChanged(val query: String) : ApprovalsListActions
}
