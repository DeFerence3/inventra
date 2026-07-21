package com.deference.inventra.presentation.approvals.list

import com.deference.inventra.domain.model.approvals.ApprovalDetails
import com.deference.inventra.domain.model.approvals.ApprovalItem
import com.deference.inventra.domain.model.approvals.ApprovalRequestType

data class ApprovalsListState(
    val approvals: List<ApprovalItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    
    val selectedStatus: String = "Pending",
    val searchQuery: String = "",
    val selectedTransType: ApprovalRequestType = ApprovalRequestType.ALL,
    val isRefreshing: Boolean = false,
    
    val selectedApprovalDetails: ApprovalDetails? = null,
    val isLoadingDetails: Boolean = false,
    val isDetailsModalVisible: Boolean = false,
    
    val comment: String = "",
    val isPerformingAction: Boolean = false,
    val actionError: String? = null
)
