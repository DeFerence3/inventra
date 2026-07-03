@file:OptIn(FlowPreview::class)

package com.deference.inventra.presentation.approvals.approve

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deference.inventra.core.utils.network.RequestState
import com.deference.inventra.domain.model.approvals.ApprovalActionRequest
import com.deference.inventra.domain.usecase.GetApprovalDetailsUseCase
import com.deference.inventra.domain.usecase.GetPurchaseOrderItemsSummaryUseCase
import com.deference.inventra.domain.usecase.PerformApprovalActionUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ApproveVM.Factory::class)
class ApproveVM @AssistedInject constructor(
    @Assisted val approvalId: String,
    @Assisted val transUuId: List<String>,
    private val getApprovalDetailsUseCase: GetApprovalDetailsUseCase,
    private val getPurchaseOrderItemsSummaryUseCase: GetPurchaseOrderItemsSummaryUseCase,
    private val performApprovalActionUseCase: PerformApprovalActionUseCase,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(approvalId: String, transUuId: List<String>): ApproveVM
    }

    private val _state = MutableStateFlow(ApproveState())
    val state: StateFlow<ApproveState> = _state.asStateFlow()

    private val _eventFlow = Channel<ApproveEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        fetchApprovalDetails(approvalId)
    }

    fun onAction(action: ApproveAction) {
        when (action) {
            ApproveAction.OnDismissDetails -> {
                _state.update {
                    it.copy(
                        isDetailsModalVisible = false,
                        selectedApprovalDetails = null,
                        comment = "",
                        commentError = null
                    )
                }
            }

            is ApproveAction.OnCommentChange -> {
                _state.update { it.copy(comment = action.comment) }
            }

            is ApproveAction.OnApprove -> {
                performAction("Approved")
            }

            is ApproveAction.OnReject -> {
                if (_state.value.comment.isBlank()) {
                    _state.update { it.copy(commentError = "Comment is mandatory for rejection") }
                } else {
                    performAction("Rejected")
                }
            }

            ApproveAction.Refresh -> {
                fetchApprovalDetails(approvalId)
            }

            is ApproveAction.OnSearchQueryChanged -> {
                _state.update { it.copy(searchQuery = action.query) }
            }

            ApproveAction.OnDismissError -> _state.update { it.copy(error = null) }
        }
    }

    private fun fetchApprovalDetails(approvalId: String) {
        viewModelScope.launch {
            getApprovalDetailsUseCase(approvalId).collectLatest { result ->
                when (result) {
                    is RequestState.Loading -> {
                        _state.update { it.copy(isLoadingDetails = true, commentError = null) }
                    }

                    is RequestState.Success -> {
                        _state.update {
                            it.copy(
                                isLoadingDetails = false,
                                selectedApprovalDetails = result.data
                            )
                        }
                        fetchItems(transUuId)
                    }

                    is RequestState.Error -> {
                        _state.update { it.copy(isLoadingDetails = false, error = result.message) }
                        _eventFlow.send(ApproveEvent.Error(result.message))
                    }
                }
            }
        }
    }

    private fun fetchItems(transUuid: List<String>) {
        viewModelScope.launch {
            getPurchaseOrderItemsSummaryUseCase(transUuid).collectLatest { result ->
                when (result) {
                    is RequestState.Loading -> {
                        _state.update { it.copy(isLoadingItems = true) }
                    }

                    is RequestState.Success -> {
                        val items = result.data.flatMap { it.items }
                        _state.update { it.copy(isLoadingItems = false, items = items) }
                    }

                    is RequestState.Error -> {
                        _state.update { it.copy(isLoadingItems = false) }
                        _eventFlow.send(ApproveEvent.Error("Failed to load items: ${result.message}"))
                    }
                }
            }
        }
    }

    private fun performAction(status: String) {
        val approvalDetails = _state.value.selectedApprovalDetails ?: return
        val step = approvalDetails.steps.firstOrNull() ?: return
        viewModelScope.launch {
            val request = ApprovalActionRequest(
                transUuid = approvalDetails.transUuid,
                comment = _state.value.comment,
                instanceId = step.instanceId,
                instanceStepId = step.id,
                transType = approvalDetails.transType,
            )
            performApprovalActionUseCase(request, status).collectLatest { result ->
                when (result) {
                    is RequestState.Loading -> {
                        _state.update {
                            it.copy(
                                isPerformingAction = true,
                                commentError = null,
                                error = null
                            )
                        }
                    }

                    is RequestState.Success -> {
                        _state.update {
                            it.copy(
                                isPerformingAction = false,
                                selectedApprovalDetails = null,
                                comment = ""
                            )
                        }
                        _eventFlow.send(ApproveEvent.Success)
                    }

                    is RequestState.Error -> {
                        _state.update {
                            it.copy(
                                isPerformingAction = false,
                                error = result.message
                            )
                        }
                    }
                }
            }
        }
    }
}
