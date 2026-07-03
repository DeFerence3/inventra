package com.deference.inventra.presentation.supplier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deference.inventra.core.utils.network.RequestState
import com.deference.inventra.domain.usecase.SearchSupplierUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierVM @Inject constructor(
    private val searchSupplierUseCase: SearchSupplierUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SupplierState())
    val state: StateFlow<SupplierState> = _state.asStateFlow()

    private val _eventFlow = Channel<SupplierEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        loadSuppliers()
    }

    fun onAction(action: SupplierActions) {
        when (action) {
            is SupplierActions.OnSearchQueryChanged -> {
                updateState { it.copy(searchQuery = action.query, page = 1, isLastPage = false, suppliers = emptyList()) }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500)
                    loadSuppliers()
                }
            }
            SupplierActions.LoadSuppliers -> loadSuppliers()
            SupplierActions.LoadNextPage -> {
                if (!state.value.isLoading && !state.value.isLastPage) {
                    updateState { it.copy(page = it.page + 1) }
                    loadSuppliers()
                }
            }
            SupplierActions.Refresh -> {
                updateState { it.copy(page = 1, isLastPage = false, suppliers = emptyList()) }
                loadSuppliers()
            }
        }
    }

    private fun loadSuppliers() {
        viewModelScope.launch {
            searchSupplierUseCase(
                name = state.value.searchQuery.ifEmpty { null },
                page = state.value.page,
                pageSize = state.value.pageSize
            ).collectLatest { result ->
                when (result) {
                    is RequestState.Loading -> {
                        updateState { it.copy(isLoading = true) }
                    }
                    is RequestState.Success -> {
                        val newSuppliers = result.data.items
                        updateState {
                            it.copy(
                                isLoading = false,
                                suppliers = it.suppliers + newSuppliers,
                                isLastPage = newSuppliers.size < it.pageSize
                            )
                        }
                    }
                    is RequestState.Error -> {
                        updateState { it.copy(isLoading = false) }
                        _eventFlow.send(SupplierEvent.Error(result.message))
                    }
                }
            }
        }
    }

    private inline fun updateState(update: (SupplierState) -> SupplierState) {
        _state.value = update(_state.value)
    }
}
