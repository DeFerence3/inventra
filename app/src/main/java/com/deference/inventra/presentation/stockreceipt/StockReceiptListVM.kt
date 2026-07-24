package com.deference.inventra.presentation.stockreceipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deference.inventra.core.utils.network.RequestState
import com.deference.inventra.domain.usecase.GetStockReceiptsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockReceiptListVM @Inject constructor(
    private val getStockReceiptsUseCase: GetStockReceiptsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(StockReceiptListState())
    val state: StateFlow<StockReceiptListState> = _state.asStateFlow()

    private val _eventFlow = Channel<StockReceiptListEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        fetchReceipts()
    }

    fun onAction(action: StockReceiptListActions) {
        when (action) {
            is StockReceiptListActions.OnStatusFilterChange -> {
                _state.update { it.copy(selectedStatus = action.status) }
                fetchReceipts()
            }
            is StockReceiptListActions.OnSearchQueryChanged -> {
                _state.update { it.copy(searchQuery = action.query) }
            }
            StockReceiptListActions.Refresh -> {
                fetchReceipts(isRefreshing = true)
            }
        }
    }

    fun fetchReceipts(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            getStockReceiptsUseCase(
                status = _state.value.selectedStatus,
                page = 1,
                pageSize = 100
            ).collect { result ->
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
                                receipts = result.data.items,
                                error = null
                            )
                        }
                    }
                    is RequestState.Error -> {
                        _state.update { it.copy(isLoading = false, isRefreshing = false, error = result.message) }
                        _eventFlow.send(StockReceiptListEvent.Error(result.message))
                    }
                }
            }
        }
    }
}
