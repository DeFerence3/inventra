package com.deference.inventra.presentation.orderItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deference.inventra.core.utils.network.RequestState
import com.deference.inventra.core.utils.now
import com.deference.inventra.domain.model.grn.GrnRequest
import com.deference.inventra.domain.usecase.GetOrderItemsByPurchaseOrdersUseCase
import com.deference.inventra.domain.usecase.SaveGrnUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

@HiltViewModel(assistedFactory = OrderItemListVM.Factory::class)
class OrderItemListVM @AssistedInject constructor(
    @Assisted val supplierId: Int,
    @Assisted val supplierName: String,
    @Assisted val poUUIDs: List<String>,
    private val getOrderItemsByPurchaseOrdersUseCase: GetOrderItemsByPurchaseOrdersUseCase,
    private val saveGrnUseCase: SaveGrnUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(supplierId: Int,supplierName: String,poUUIDs: List<String>): OrderItemListVM
    }

    init {
        fetchItems(poUUIDs)
    }

    private val _state = MutableStateFlow(OrderItemListState())
    val state: StateFlow<OrderItemListState> = _state.asStateFlow()

    private val _eventFlow = Channel<OrderItemListEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    fun onAction(action: OrderItemListActions) {
        when (action) {
            is OrderItemListActions.SaveGrn -> saveGrn()
            OrderItemListActions.DismissError -> _state.update { it.copy(error = null) }
        }
    }

    private fun saveGrn() {
        val items = _state.value.items.map{ it.toItem() }

        val grnRequest = GrnRequest(
            items = items,
            netAmount = items.sumOf { it.netAmount ?: 0.0 },
            taxAmount = items.sumOf { it.taxAmount ?: 0.0 },
            grossAmount = items.sumOf { it.grossAmount ?: 0.0 },
            transDate = LocalDateTime.now().toString(),
            locationId = items.first().locationId,
            vendorId = supplierId,
            discountAmount = 0.0,
            taxableAmount = items.sumOf { it.netAmount ?: 0.0 },
            isInvoiced = false,
            transNo = "TFG",
            vendorName = supplierName,
            currencyCode = "AED",
            baseCurrencyCode = "AED"
        )

        viewModelScope.launch {
            saveGrnUseCase(grnRequest).collectLatest { result ->
                when (result) {
                    is RequestState.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is RequestState.Success -> {
                        _state.update { it.copy(isLoading = false, error = null) }
                        _eventFlow.send(OrderItemListEvent.Success)
                    }
                    is RequestState.Error -> {
                        _state.update { it.copy(isLoading = false, error = result.message) }
//                        _eventFlow.send(OrderItemListEvent.Error(result.message))
                    }
                }
            }
        }
    }

    private fun fetchItems(poUUIDs: List<String>) {
        viewModelScope.launch {
            getOrderItemsByPurchaseOrdersUseCase(poUUIDs).collectLatest { result ->
                when (result) {
                    is RequestState.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is RequestState.Success -> {
                        _state.update { it.copy(isLoading = false, items = result.data, error = null) }
                    }
                    is RequestState.Error -> {
                        _state.update { it.copy(isLoading = false, error = result.message) }
                        _eventFlow.send(OrderItemListEvent.Error(result.message))
                    }
                }
            }
        }
    }
}
