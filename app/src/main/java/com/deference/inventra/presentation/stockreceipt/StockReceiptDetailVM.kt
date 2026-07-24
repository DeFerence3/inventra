package com.deference.inventra.presentation.stockreceipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deference.inventra.core.utils.network.RequestState
import com.deference.inventra.domain.model.stock.StockReceiptRequest
import com.deference.inventra.domain.model.stock.StockReceiptRequestItem
import com.deference.inventra.domain.usecase.GetStockReceiptDetailsUseCase
import com.deference.inventra.domain.usecase.SaveStockReceiptUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface StockReceiptDetailEvent {
    data class Error(val message: String) : StockReceiptDetailEvent
    data object Success : StockReceiptDetailEvent
}

@HiltViewModel(assistedFactory = StockReceiptDetailVM.Factory::class)
class StockReceiptDetailVM @AssistedInject constructor(
    @Assisted private val transNo: String,
    private val saveStockReceiptUseCase: SaveStockReceiptUseCase,
    private val getStockReceiptDetailsUseCase: GetStockReceiptDetailsUseCase,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(transNo: String): StockReceiptDetailVM
    }

    private val _state = MutableStateFlow(StockReceiptDetailState())
    val state: StateFlow<StockReceiptDetailState> = _state
        .onStart {
            loadDetails()
        }
        .stateIn(
            viewModelScope,
            kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            _state.value
        )

    private val _eventFlow = Channel<StockReceiptDetailEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private fun loadDetails() {
        viewModelScope.launch {
            getStockReceiptDetailsUseCase(transNo).collect { result ->
                when (result) {
                    is RequestState.Loading -> _state.update { it.copy(isLoading = true) }
                    is RequestState.Success -> _state.update {
                        it.copy(isLoading = false, receipt = result.data, error = null)
                    }

                    is RequestState.Error -> {
                        _state.update { it.copy(isLoading = false, error = result.message) }
                        _eventFlow.send(StockReceiptDetailEvent.Error(result.message))
                    }
                }
            }
        }
    }

    fun acceptReceipt() {
        submitReceipt(status = 1, isCancelled = false)
    }

    fun declineReceipt() {
        submitReceipt(status = 2, isCancelled = true)
    }

    private fun submitReceipt(status: Int, isCancelled: Boolean) {
        val receipt = _state.value.receipt ?: return
        val items = receipt.items.map { item ->
            StockReceiptRequestItem(
                itemId = item.itemId,
                itemCode = item.itemCode,
                itemName = item.itemName,
                unitId = item.unitId,
                unitName = item.unitName,
                baseUnitId = item.baseUnitId,
                baseUnitName = item.baseUnitName,
                storeUnitId = item.storeUnitId,
                storeUnitName = item.storeUnitName,
                conversionFactor = item.conversionFactor,
                conversionFactorBUToSU = item.conversionFactorBUToSU,
                issuedQty = item.issueQty,
                receiptQty = item.issueQty,
                averagePrice = item.averagePrice,
                lastPurchasePrice = item.lastPurchasePrice,
                total = item.total,
                qtyInBaseUnit = item.qtyInBaseUnit.toInt(),
                notes = item.notes,
                itemGroupId = item.itemGroupId,
                itemGroupName = item.itemGroupName,
                overGroupId = item.overGroupId,
                overGroupName = item.overGroupName,
                majorGroupId = item.majorGroupId,
                majorGroupName = item.majorGroupName
            )
        }

        val request = StockReceiptRequest(
            transNo = receipt.transNo,
            transDate = receipt.transDate,
            issueLocationId = receipt.issueFromLocationId,
            issueLocationName = receipt.issueFromLocationName,
            receiptLocationId = receipt.receiptToLocationId,
            receiptLocationName = receipt.receiptToLocationName,
            transferNos = receipt.transNo,
            transferIDs = receipt.id.toString(),
            remarks = receipt.remarks,
            isCancelled = isCancelled,
            isDraft = false,
            status = status,
            currencyCode = "AED",
            baseCurrencyCode = "AED",
            currencyConversionRate = 1,
            stockReceiptRequestItems = items
        )

        viewModelScope.launch {
            saveStockReceiptUseCase(request).collect { result ->
                when (result) {
                    is RequestState.Loading -> _state.update { it.copy(isLoading = true) }
                    is RequestState.Success -> {
                        _state.update { it.copy(isLoading = false, isSubmitted = true) }
                        _eventFlow.send(StockReceiptDetailEvent.Success)
                    }

                    is RequestState.Error -> {
                        _state.update { it.copy(isLoading = false, error = result.message) }
                        _eventFlow.send(StockReceiptDetailEvent.Error(result.message))
                    }
                }
            }
        }
    }
}
