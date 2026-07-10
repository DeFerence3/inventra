package com.deference.inventra.presentation.spotcheck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deference.inventra.core.utils.network.RequestState
import com.deference.inventra.domain.model.purchase.Item
import com.deference.inventra.domain.model.spotcheck.SpotCheckRequestBody
import com.deference.inventra.domain.usecase.GetNextSerialNoUseCase
import com.deference.inventra.domain.usecase.SubmitSpotCheckUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotCheckVM @Inject constructor(
    private val submitSpotCheckUseCase: SubmitSpotCheckUseCase,
    private val getNextSerialNoUseCase: GetNextSerialNoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SpotCheckState())
    val state: StateFlow<SpotCheckState> = _state.asStateFlow()

    private val _eventFlow = Channel<SpotCheckEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        fetchProposedSerialNo()
    }

    private fun fetchProposedSerialNo() {
        viewModelScope.launch {
            getNextSerialNoUseCase(mapOf("transactionType" to "SA")).collect { result ->
                when (result) {
                    is RequestState.Loading -> {
                        updateState { it.copy(isLoading = true) }
                    }
                    is RequestState.Success -> {
                        updateState { it.copy(isLoading = false, spotCheckNo = result.data.proposedSerial) }
                    }
                    is RequestState.Error -> {
                        updateState { it.copy(isLoading = false, error = result.message) }
                        _eventFlow.send(SpotCheckEvent.Error(result.message))
                    }
                }
            }
        }
    }


    fun onAction(action: SpotCheckActions) {
        when (action) {
            is SpotCheckActions.OnSpotCheckNoChanged -> {
                updateState { it.copy(spotCheckNo = action.no) }
            }
            is SpotCheckActions.OnSpotCheckDateChanged -> {
                updateState { it.copy(spotCheckDate = action.date) }
            }
            is SpotCheckActions.OnInternalNotesChanged -> {
                updateState { it.copy(internalNotes = action.notes) }
            }
            is SpotCheckActions.OnItemQtyChanged -> {
                updateState { state ->
                    val newItems = state.items.toMutableList()
                    if (action.index in newItems.indices) {
                        newItems[action.index] = newItems[action.index].copy(physicalQty = action.qty)
                    }
                    state.copy(items = newItems)
                }
            }
            is SpotCheckActions.OnItemRemarksChanged -> {
                updateState { state ->
                    val newItems = state.items.toMutableList()
                    if (action.index in newItems.indices) {
                        newItems[action.index] = newItems[action.index].copy(remarks = action.remarks)
                    }
                    state.copy(items = newItems)
                }
            }
            SpotCheckActions.AddItem -> {
                updateState { it.copy(items = listOf(SpotCheckItemInput()) + it.items) }
            }
            is SpotCheckActions.RemoveItem -> {
                updateState { state ->
                    val newItems = state.items.toMutableList()
                    if (action.index in newItems.indices && newItems.size > 1) {
                        newItems.removeAt(action.index)
                    }
                    state.copy(items = newItems)
                }
            }
            SpotCheckActions.Submit -> {
                submitSpotCheck()
            }
            is SpotCheckActions.SelectItem -> {
                updateState { state ->
                    val newItems = state.items.toMutableList()
                    if (action.index in newItems.indices) {
                        newItems[action.index] = newItems[action.index].copy(
                            item = action.item,
                        )
                    }
                    state.copy(items = newItems)
                }
            }
            is SpotCheckActions.SelectLocation -> {
                updateState { state ->
                    val newItems = state.items.toMutableList()
                    if (action.index in newItems.indices) {
                        newItems[action.index] = newItems[action.index].copy(location = action.location)
                    }
                    state.copy(items = newItems)
                }
            }
        }
    }

    private fun submitSpotCheck() {
        val currentState = _state.value
        val mappedItems = currentState.items.map { input ->
            Item(
                itemCode = input.itemCode,
                itemName = input.itemName,
                unitName = "Unit",
                baseUnitName = "Unit",
                requiredQty = input.physicalQty.toDoubleOrNull() ?: 0.0,
                pricePerUnit = 0.0,
                pricePerBaseUnit = 0.0,
                grossAmount = 0.0,
                vendorName = "",
                locationName = input.location,
                stockOnHand = 0,
                itemGroupName = input.item?.itemGroup,
                overGroupName = input.item?.overGroup,
                majorGroupName = input.item?.majorGroup,
            )
        }

        val requestBody = SpotCheckRequestBody(
            transNo = currentState.spotCheckNo,
            transDate = currentState.spotCheckDate,
            locationId = 1,
            remarks = currentState.internalNotes,
            isCancelled = false,
            isDraft = false,
            status = 1,
            currencyCode = "USD",
            baseCurrencyCode = "USD",
            currencyConversionRate = 1,
            items = mappedItems
        )

        viewModelScope.launch {
            submitSpotCheckUseCase(requestBody).collect { result ->
                when (result) {
                    is RequestState.Loading -> {
                        updateState { it.copy(isLoading = true) }
                    }
                    is RequestState.Success -> {
                        updateState { it.copy(isLoading = false, isSubmitted = true) }
                        _eventFlow.send(SpotCheckEvent.Success)
                    }
                    is RequestState.Error -> {
                        updateState { it.copy(isLoading = false, error = result.message) }
                        _eventFlow.send(SpotCheckEvent.Error(result.message))
                    }
                }
            }
        }
    }

    private inline fun updateState(update: (SpotCheckState) -> SpotCheckState) {
        _state.value = update(_state.value)
    }
}





