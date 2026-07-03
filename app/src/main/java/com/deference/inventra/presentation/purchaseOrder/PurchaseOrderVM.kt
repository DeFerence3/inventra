package com.deference.inventra.presentation.purchaseOrder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deference.inventra.core.utils.network.RequestState
import com.deference.inventra.domain.usecase.GetPurchaseOrdersBySupplierUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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

@HiltViewModel(assistedFactory = PurchaseOrderVM.Factory::class)
class PurchaseOrderVM @AssistedInject constructor(
    @Assisted val supplierId: Int,
    private val getPurchaseOrdersBySupplierUseCase: GetPurchaseOrdersBySupplierUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(supplierId: Int): PurchaseOrderVM
    }

    private val _state = MutableStateFlow(PurchaseOrderState())
    val state: StateFlow<PurchaseOrderState> = _state.asStateFlow()

    private val _eventFlow = Channel<PurchaseOrderEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        updateState { it.copy(supplierId = supplierId, page = 1, purchaseOrders = emptyList(), isLastPage = false) }
        loadPurchaseOrders()
    }

    private var searchJob: Job? = null

    fun onAction(action: PurchaseOrderActions) {
        when (action) {
            is PurchaseOrderActions.OnSearchQueryChanged -> {
                updateState { it.copy(searchQuery = action.query, page = 1, isLastPage = false, purchaseOrders = emptyList()) }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500)
                    loadPurchaseOrders()
                }
            }
            is PurchaseOrderActions.OnTogglePO -> {
                val currentSelected = state.value.selectedPOUUIDs
                val newSelected = if (currentSelected.contains(action.poUUID)) {
                    currentSelected - action.poUUID
                } else {
                    currentSelected + action.poUUID
                }
                updateState { it.copy(selectedPOUUIDs = newSelected) }
            }
            PurchaseOrderActions.LoadNextPage -> {
                if (!state.value.isLoading && !state.value.isLastPage) {
                    updateState { it.copy(page = it.page + 1) }
                    loadPurchaseOrders()
                }
            }
            PurchaseOrderActions.Refresh -> {
                updateState { it.copy(page = 1, isLastPage = false, purchaseOrders = emptyList()) }
                loadPurchaseOrders()
            }
        }
    }

    private fun loadPurchaseOrders() {
        val supplierId = state.value.supplierId
        if (supplierId == 0) return

        viewModelScope.launch {
            getPurchaseOrdersBySupplierUseCase(
                name = state.value.searchQuery.ifEmpty { null },
                page = state.value.page,
                pageSize = state.value.pageSize,
                supplierId = supplierId
            ).collectLatest { result ->
                when (result) {
                    is RequestState.Loading -> {
                        updateState { it.copy(isLoading = true) }
                    }
                    is RequestState.Success -> {
                        val newPOs = result.data.items
                        updateState {
                            it.copy(
                                isLoading = false,
                                purchaseOrders = it.purchaseOrders + newPOs,
                                isLastPage = newPOs.size < it.pageSize
                            )
                        }
                    }
                    is RequestState.Error -> {
                        updateState { it.copy(isLoading = false) }
                        _eventFlow.send(PurchaseOrderEvent.Error(result.message))
                    }
                }
            }
        }
    }

    private inline fun updateState(update: (PurchaseOrderState) -> PurchaseOrderState) {
        _state.value = update(_state.value)
    }
}
