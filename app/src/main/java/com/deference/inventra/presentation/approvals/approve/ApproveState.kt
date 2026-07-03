package com.deference.inventra.presentation.approvals.approve

import com.deference.inventra.domain.model.approvals.ApprovalDetails
import com.deference.inventra.domain.model.approvals.ApprovalItem
import com.deference.inventra.domain.model.purchase.Item

data class ApproveState(
    val approvals: List<ApprovalItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    
    val selectedStatus: String = "Pending",
    val searchQuery: String = "",
    val selectedTransType: String = "All",
    val isRefreshing: Boolean = false,
    
    val selectedApprovalDetails: ApprovalDetails? = null,
    val isLoadingDetails: Boolean = false,
    val isDetailsModalVisible: Boolean = false,

    val items: List<Item> = emptyList(),
    val isLoadingItems: Boolean = false,
    
    val comment: String = "",
    val isPerformingAction: Boolean = false,
    val commentError: String? = null
)
