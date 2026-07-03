@file:OptIn(FlowPreview::class)

package com.deference.inventra.presentation.approvals.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deference.inventra.core.utils.network.RequestState
import com.deference.inventra.domain.model.approvals.ApprovalActionRequest
import com.deference.inventra.domain.usecase.GetApprovalDetailsUseCase
import com.deference.inventra.domain.usecase.GetApprovalsUseCase
import com.deference.inventra.domain.usecase.PerformApprovalActionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ApprovalsListVM @Inject constructor(
    private val getApprovalsUseCase: GetApprovalsUseCase,
    private val getApprovalDetailsUseCase: GetApprovalDetailsUseCase,
    private val performApprovalActionUseCase: PerformApprovalActionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ApprovalsListState())
    val state: StateFlow<ApprovalsListState> = _state.asStateFlow()

    private val _eventFlow = Channel<ApprovalsListEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        fetchApprovals()
        viewModelScope.launch {
            _state.map { it.searchQuery }
                .debounce(500.milliseconds)
                .distinctUntilChanged()
                .collectLatest { query ->
                    fetchApprovals(search = query)
                }
        }
    }

    fun onAction(action: ApprovalsListActions) {
        when (action) {
            is ApprovalsListActions.OnStatusFilterChange -> {
                _state.update { it.copy(searchQuery = "",selectedStatus = action.status) }
                fetchApprovals()
            }
            is ApprovalsListActions.OnTransTypeFilterChange -> {
                _state.update { it.copy(searchQuery = "",selectedTransType = action.transType) }
                fetchApprovals()
            }
            is ApprovalsListActions.OnApprovalClick -> {
                // This is now handled by navigation
            }
            ApprovalsListActions.OnDismissDetails -> {
                _state.update { it.copy(isDetailsModalVisible = false, selectedApprovalDetails = null, comment = "", actionError = null) }
            }
            ApprovalsListActions.Refresh -> {
                fetchApprovals(isRefreshing = true)
            }

            is ApprovalsListActions.OnSearchQueryChanged -> {
                _state.update { it.copy(searchQuery = action.query) }
            }
        }
    }

    private fun fetchApprovals(isRefreshing: Boolean = false,search: String? = null) {
        viewModelScope.launch {
            getApprovalsUseCase(status = _state.value.selectedStatus, search = search,transType = _state.value.selectedTransType).collectLatest { result ->
                when (result) {
                    is RequestState.Loading -> {
                        if (isRefreshing) {
                            _state.update { it.copy(isRefreshing = true) }
                        } else {
                            _state.update { it.copy(isLoading = true, error = null) }
                        }
                    }
                    is RequestState.Success -> {
                        _state.update { 
                            it.copy(
                                isLoading = false, 
                                isRefreshing = false,
                                approvals = result.data.items, 
                                error = null
                            ) 
                        }
                    }
                    is RequestState.Error -> {
                        _state.update { it.copy(isLoading = false, isRefreshing = false, error = result.message) }
                        _eventFlow.send(ApprovalsListEvent.Error(result.message))
                    }
                }
            }
        }
    }
}
