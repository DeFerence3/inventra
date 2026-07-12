package com.deference.inventra.presentation.purchaserequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deference.inventra.core.utils.network.RequestState
import com.deference.inventra.domain.model.pr.PrItem
import com.deference.inventra.domain.model.pr.PrRequestBody
import com.deference.inventra.domain.usecase.GetNextSerialNoUseCase
import com.deference.inventra.domain.usecase.GetUnitsUseCase
import com.deference.inventra.domain.usecase.SubmitPurchaseRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseRequestVM @Inject constructor(
    private val submitPurchaseRequestUseCase: SubmitPurchaseRequestUseCase,
    private val getNextSerialNoUseCase: GetNextSerialNoUseCase,
    private val getUnitsUseCase: GetUnitsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PurchaseRequestState())
    val state: StateFlow<PurchaseRequestState> = _state.asStateFlow()

    private val _eventFlow = Channel<PurchaseRequestEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        fetchProposedSerialNo()
    }

    private fun fetchProposedSerialNo() {
        viewModelScope.launch {
            getNextSerialNoUseCase(mapOf("transactionType" to "PR")).collect { result ->
                when (result) {
                    is RequestState.Loading -> updateState { it.copy(isLoading = true) }
                    is RequestState.Success -> updateState {
                        it.copy(isLoading = false, requestNo = result.data.proposedSerial)
                    }
                    is RequestState.Error -> {
                        updateState { it.copy(isLoading = false, error = result.message) }
                        _eventFlow.send(PurchaseRequestEvent.Error(result.message))
                    }
                }
            }
        }
    }

    fun onAction(action: PurchaseRequestActions) {
        when (action) {
            is PurchaseRequestActions.OnRequestNoChanged -> {
                updateState { it.copy(requestNo = action.no) }
            }
            is PurchaseRequestActions.OnRequestDateChanged -> {
                updateState { it.copy(requestDate = action.date) }
            }
            is PurchaseRequestActions.OnInternalNotesChanged -> {
                updateState { it.copy(internalNotes = action.notes) }
            }
            is PurchaseRequestActions.OnItemExpectedDateChanged -> {
                updateItem(action.index) { it.copy(expectedDate = action.date) }
            }
            is PurchaseRequestActions.OnItemQtyChanged -> {
                updateItem(action.index) { it.copy(requestedQty = action.qty, requestedQtyError = null) }
            }
            is PurchaseRequestActions.OnItemRemarksChanged -> {
                updateItem(action.index) { it.copy(remarks = action.remarks) }
            }
            is PurchaseRequestActions.SelectLocation -> {
                updateItem(action.index) { it.copy(location = action.location, locationError = null) }
            }
            is PurchaseRequestActions.SelectItem -> {
                updateItem(action.index) { it.copy(item = action.item, itemError = null) }
                fetchUnits(action.index, action.item.baseUnitId)
            }
            is PurchaseRequestActions.SelectUnit -> {
                updateItem(action.index) { it.copy(unit = action.unit, unitError = null) }
            }
            PurchaseRequestActions.AddItem -> {
                updateState { it.copy(items = listOf(PurchaseRequestItemInput()) + it.items) }
            }
            is PurchaseRequestActions.RemoveItem -> {
                updateState { state ->
                    val newItems = state.items.toMutableList()
                    if (action.index in newItems.indices && newItems.size > 1) {
                        newItems.removeAt(action.index)
                    }
                    state.copy(items = newItems)
                }
            }
            PurchaseRequestActions.Submit -> submitPurchaseRequest()
        }
    }

    private fun fetchUnits(index: Int, baseUnitId: Int) {
        viewModelScope.launch {
            getUnitsUseCase(baseUnitId).collect { result ->
                when (result) {
                    is RequestState.Loading -> updateState { it.copy(isLoading = true) }
                    is RequestState.Success -> {
                        val units = result.data.items
                        updateState { state ->
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
                        updateState { it.copy(isLoading = false, error = result.message) }
                        _eventFlow.send(PurchaseRequestEvent.Error(result.message))
                    }
                }
            }
        }
    }

    private fun submitPurchaseRequest() {
        val currentState = _state.value
        val validatedItems = currentState.items.map { input ->
            val requestedQty = input.requestedQty.toDoubleOrNull()
            input.copy(
                locationError = if (input.location == null) "Location required" else null,
                itemError = if (input.item == null) "Item required" else null,
                unitError = if (input.item != null && input.unit == null) "Unit required" else null,
                requestedQtyError = if (requestedQty == null || requestedQty <= 0.0) "Req qty required" else null,
            )
        }

        if (validatedItems.any { it.locationError != null || it.itemError != null || it.unitError != null || it.requestedQtyError != null }) {
            updateState { it.copy(items = validatedItems) }
            viewModelScope.launch { _eventFlow.send(PurchaseRequestEvent.Error("Fix highlighted fields")) }
            return
        }

        updateState { it.copy(items = validatedItems) }
        val firstLocation = checkNotNull(validatedItems.first().location)
        val items = validatedItems.map { input ->
            val item = checkNotNull(input.item)
            val location = checkNotNull(input.location)
            val unit = checkNotNull(input.unit)
            val qty = input.requestedQty.toDoubleOrNull()?.toInt() ?: 0
            PrItem(
                itemId = item.itemId,
                itemName = item.itemName,
                itemCode = item.itemCode ?: "N/A",
                unitId = unit.id,
                unitName = unit.name,
                baseUnitId = item.baseUnitId,
                baseUnitName = unit.baseUnitName,
                itemGroupId = 0,
                itemGroupName = item.itemGroup,
                overGroupId = 0,
                overGroupName = item.overGroup,
                majorGroupId = 0,
                majorGroupName = item.majorGroup,
                vendorId = 0,
                vendorName = "N/A",
                vendorGroupId = 0,
                vendorGroupName = "N/A",
                requestedQuantity = qty,
                expectedDate = input.expectedDate,
                remarks = input.remarks,
                locationId = location.id,
                locationName = location.name,
                stockOnHand = 0,
                stockOnOrder = 0,
                lastPurchaseDate = null,
                lastPurchasePrice = 0,
                netAmountLP = 0,
                vatPercentage = 0,
                vatAmountLP = 0,
                grossAmountLP = 0,
                pricePerUnit = 0,
                pricePerBaseUnit = 0,
                quantityInBaseUnit = qty,
                conversionFactor = unit.qtyInBaseUnit.toInt(),
                discountPercentage = 0,
                discountAmount = 0,
                netAmount = 0,
                vatAmount = 0,
                grossAmount = 0,
                authorisationLevel = 0,
                isHACCPRequired = false,
                expiryDays = 0,
                isStockItem = true,
                conversionFactorBUToSU = 1,
                rateNumberId = 0,
                storeUnitId = unit.id,
                storeUnitName = unit.name,
                purchaseItemId = item.itemId,
                purchaseItemName = item.itemName,
                purchaseItemCode = item.itemCode ?: ""
            )
        }

        val requestBody = PrRequestBody(
            transNo = currentState.requestNo,
            transDate = currentState.requestDate.toString(),
            locationId = firstLocation.id,
            locationName = firstLocation.name,
            expectedDate = validatedItems.first().expectedDate,
            internalNotes = currentState.internalNotes,
            status = 1,
            currencyCode = "USD",
            baseCurrencyCode = "USD",
            currencyConversionRate = 1,
            userLevel = 0,
            items = items
        )

        viewModelScope.launch {
            submitPurchaseRequestUseCase(requestBody).collect { result ->
                when (result) {
                    is RequestState.Loading -> updateState { it.copy(isLoading = true) }
                    is RequestState.Success -> {
                        updateState { it.copy(isLoading = false, isSubmitted = true) }
                        _eventFlow.send(PurchaseRequestEvent.Success)
                    }
                    is RequestState.Error -> {
                        updateState { it.copy(isLoading = false, error = result.message) }
                        _eventFlow.send(PurchaseRequestEvent.Error(result.message))
                    }
                }
            }
        }
    }

    private fun updateItem(index: Int, update: (PurchaseRequestItemInput) -> PurchaseRequestItemInput) {
        updateState { state ->
            val newItems = state.items.toMutableList()
            if (index in newItems.indices) {
                newItems[index] = update(newItems[index])
            }
            state.copy(items = newItems)
        }
    }

    private inline fun updateState(update: (PurchaseRequestState) -> PurchaseRequestState) {
        _state.value = update(_state.value)
    }
}
