package com.deference.inventra.presentation.stockrequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deference.inventra.core.utils.network.RequestState
import com.deference.inventra.domain.model.stock.StockRequestItem
import com.deference.inventra.domain.model.stock.StockRequestRequestBody
import com.deference.inventra.domain.usecase.GetNextSerialNoUseCase
import com.deference.inventra.domain.usecase.GetUnitsUseCase
import com.deference.inventra.domain.usecase.SubmitStockRequestUseCase
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
class StockRequestVM @Inject constructor(
    private val submitStockRequestUseCase: SubmitStockRequestUseCase,
    private val getNextSerialNoUseCase: GetNextSerialNoUseCase,
    private val getUnitsUseCase: GetUnitsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(StockRequestState())
    val state: StateFlow<StockRequestState> = _state.asStateFlow()

    private val _eventFlow = Channel<StockRequestEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        fetchProposedSerialNo()
    }

    private fun fetchProposedSerialNo() {
        viewModelScope.launch {
            getNextSerialNoUseCase(mapOf("transactionType" to "SRQ")).collect { result ->
                when (result) {
                    is RequestState.Loading -> _state.update { it.copy(isLoading = true) }
                    is RequestState.Success -> _state.update {
                        it.copy(isLoading = false, requestNo = result.data.proposedSerial)
                    }
                    is RequestState.Error -> {
                        _state.update { it.copy(isLoading = false, error = result.message) }
                        _eventFlow.send(StockRequestEvent.Error(result.message))
                    }
                }
            }
        }
    }

    fun onAction(action: StockRequestActions) {
        when (action) {
            is StockRequestActions.SelectReceiptLocation -> {
                _state.update { it.copy(receiptLocation = action.location, receiptLocationError = null) }
            }
            is StockRequestActions.SelectTransferLocation -> {
                _state.update { it.copy(transferLocation = action.location, transferLocationError = null) }
            }
            is StockRequestActions.OnRemarksChanged -> {
                _state.update { it.copy(remarks = action.remarks) }
            }
            is StockRequestActions.SelectItem -> {
                updateItem(action.index) { it.copy(item = action.item, itemError = null) }
                fetchUnits(action.index, action.item.baseUnitId)
            }
            is StockRequestActions.SelectUnit -> {
                updateItem(action.index) { it.copy(unit = action.unit, unitError = null) }
            }
            is StockRequestActions.OnItemQtyChanged -> {
                updateItem(action.index) { it.copy(requestedQty = action.qty, requestedQtyError = null) }
            }
            is StockRequestActions.OnItemRemarksChanged -> {
                updateItem(action.index) { it.copy(remarks = action.remarks) }
            }
            StockRequestActions.AddItem -> {
                _state.update { it.copy(items = listOf(StockRequestItemInput()) + it.items) }
            }
            is StockRequestActions.RemoveItem -> {
                _state.update { state ->
                    val newItems = state.items.toMutableList()
                    if (action.index in newItems.indices && newItems.size > 1) {
                        newItems.removeAt(action.index)
                    }
                    state.copy(items = newItems)
                }
            }
            StockRequestActions.Submit -> submitStockRequest()
        }
    }

    private fun fetchUnits(index: Int, baseUnitId: Int) {
        viewModelScope.launch {
            getUnitsUseCase(baseUnitId).collect { result ->
                when (result) {
                    is RequestState.Loading -> _state.update { it.copy(isLoading = true) }
                    is RequestState.Success -> {
                        val units = result.data.items
                        _state.update { state ->
                            val newItems = state.items.toMutableList()
                            if (index in newItems.indices) {
                                newItems[index] = newItems[index].copy(
                                    units = units,
                                    unit = units.firstOrNull(),
                                    unitError = null
                                )
                            }
                            state.copy(isLoading = false, items = newItems)
                        }
                    }
                    is RequestState.Error -> {
                        _state.update { it.copy(isLoading = false, error = result.message) }
                        _eventFlow.send(StockRequestEvent.Error(result.message))
                    }
                }
            }
        }
    }

    private fun submitStockRequest() {
        val currentState = _state.value
        val validatedItems = currentState.items.map { input ->
            val requestedQty = input.requestedQty.toDoubleOrNull()
            input.copy(
                itemError = if (input.item == null) "Item required" else null,
                unitError = if (input.item != null && input.unit == null) "Unit required" else null,
                requestedQtyError = if (requestedQty == null || requestedQty <= 0.0) "Qty required" else null,
            )
        }

        val receiptLocationError = if (currentState.receiptLocation == null) "Receipt location required" else null
        val transferLocationError = if (currentState.transferLocation == null) "Transfer location required" else null

        if (receiptLocationError != null || transferLocationError != null || validatedItems.any { it.itemError != null || it.unitError != null || it.requestedQtyError != null }) {
            _state.update {
                it.copy(
                    items = validatedItems,
                    receiptLocationError = receiptLocationError,
                    transferLocationError = transferLocationError
                )
            }
            viewModelScope.launch { _eventFlow.send(StockRequestEvent.Error("Fix highlighted fields")) }
            return
        }

        _state.update {
            it.copy(
                items = validatedItems,
                receiptLocationError = null,
                transferLocationError = null
            )
        }

        val receiptLoc = checkNotNull(currentState.receiptLocation)
        val transferLoc = checkNotNull(currentState.transferLocation)

        val items = validatedItems.map { input ->
            val item = checkNotNull(input.item)
            val unit = checkNotNull(input.unit)
            val qty = checkNotNull(input.requestedQty.toDoubleOrNull())
            StockRequestItem.fromSearchItem(
                searchItem = item,
                unit = unit,
                qty = qty,
                notes = input.remarks,
                averagePrice = 0.0,
                lastPurchasePrice = 0.0,
                total = 0.0,
                stockOnHand = 0.0
            )
        }

        val requestBody = StockRequestRequestBody(
            transNo = currentState.requestNo,
            transDate = currentState.requestDate,
            remarks = currentState.remarks,
            requestFromLocationId = receiptLoc.id,
            requestFromLocationName = receiptLoc.name,
            requestToLocationId = transferLoc.id,
            requestToLocationName = transferLoc.name,
            status = 0,
            totalNetAmount = 0.0,
            totalVatAmount = 0.0,
            totalGrossAmount = 0.0,
            stockRequestItems = items,
        )

        viewModelScope.launch {
            submitStockRequestUseCase(requestBody).collect { result ->
                when (result) {
                    is RequestState.Loading -> _state.update { it.copy(isLoading = true) }
                    is RequestState.Success -> {
                        _state.update { it.copy(isLoading = false, isSubmitted = true) }
                        _eventFlow.send(StockRequestEvent.Success)
                    }
                    is RequestState.Error -> {
                        _state.update { it.copy(isLoading = false, error = result.message) }
                        _eventFlow.send(StockRequestEvent.Error(result.message))
                    }
                }
            }
        }
    }

    private fun updateItem(index: Int, update: (StockRequestItemInput) -> StockRequestItemInput) {
        _state.update { state ->
            val newItems = state.items.toMutableList()
            if (index in newItems.indices) {
                newItems[index] = update(newItems[index])
            }
            state.copy(items = newItems)
        }
    }
}
