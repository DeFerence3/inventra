package com.deference.inventra.presentation.orderItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deference.inventra.core.utils.network.RequestState
import com.deference.inventra.core.utils.now
import com.deference.inventra.data.remote.ItemApiService
import com.deference.inventra.domain.model.grn.GrnRequest
import com.deference.inventra.domain.model.purchase.OrderItem
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
    @Assisted val poUUIDs: List<String>?,
    private val getOrderItemsByPurchaseOrdersUseCase: GetOrderItemsByPurchaseOrdersUseCase,
    private val saveGrnUseCase: SaveGrnUseCase,
    private val itemApiService: ItemApiService
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(supplierId: Int,supplierName: String,poUUIDs: List<String>?): OrderItemListVM
    }

    private val _state: MutableStateFlow<OrderItemListState>

    init {
        if (poUUIDs != null) {
            fetchItems(poUUIDs)
            _state = MutableStateFlow(OrderItemListState(isLoading = true))
        }else{
            _state = MutableStateFlow(OrderItemListState(isLoading = false, error = null))
        }
    }

    val state: StateFlow<OrderItemListState> = _state.asStateFlow()

    private val _eventFlow = Channel<OrderItemListEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    fun onAction(action: OrderItemListActions) {
        when (action) {
            is OrderItemListActions.SaveGrn -> saveGrn(action.deliveryChallanNo, action.deliveryChallanDate)
            OrderItemListActions.DismissError -> _state.update { it.copy(error = null) }
            is OrderItemListActions.UpdateItemAmounts -> updateItemAmounts(action)
            is OrderItemListActions.SearchItems -> searchItems(action.query)
            is OrderItemListActions.AddManualItem -> addManualItem(action.item, action.qty, action.rate)
        }
    }

    private fun searchItems(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(isSearching = true) }
            try {
                val response = itemApiService.getItemList(query, 1, 50).await()
                if (response.isSuccessful) {
                    _state.update { it.copy(searchResults = response.body()?.items ?: emptyList(), isSearching = false) }
                } else {
                    _state.update { it.copy(isSearching = false, error = response.message()) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isSearching = false, error = e.localizedMessage) }
            }
        }
    }

    private fun addManualItem(item: com.deference.inventra.domain.model.purchase.Item, qty: Double, rate: Double) {
        val net = qty * rate
        val tax = net * 5.0 / 100.0
        val newItem = OrderItem(
            purchaseOrderId = 0,
            purchaseOrderItemId = 0,
            purchaseOrderItemUuid = java.util.UUID.randomUUID().toString(),
            itemId = 0,
            itemName = item.itemName,
            itemCode = item.itemCode,
            itemGroupId = 0,
            itemGroupName = "",
            majorGroupId = 0,
            majorGroupName = "",
            overGroupId = 0,
            overGroupName = "",
            unitId = 0,
            unitName = item.unitName,
            baseUnitId = 0,
            baseUnitName = item.baseUnitName,
            purchaseItemId = 0,
            purchaseItemName = item.itemName,
            requiredQty = qty,
            pricePerUnit = rate,
            pricePerBaseUnit = rate,
            receivedQty = qty,
            remarks = "",
            locationId = 1,
            locationName = item.locationName.ifEmpty { "Default Location" },
            discountPercentage = 0.0,
            discountAmount = 0.0,
            taxPercentage = 5.0,
            taxAmount = tax,
            netAmount = net,
            grossAmount = net + tax,
            expiryDays = null,
            storeUnitId = 0,
            storeUnitName = item.baseUnitName,
            isHACCPRequired = false,
            isStockItem = true,
            conversionFactorBUToSU = 1.0
        )
        _state.update { currentState ->
            currentState.copy(
                items = currentState.items + newItem,
                searchResults = emptyList() // clear search after adding
            )
        }
    }

    private fun updateItemAmounts(action: OrderItemListActions.UpdateItemAmounts) {
        val qty = action.qty.coerceAtLeast(0.0)
        val rate = action.rate.coerceAtLeast(0.0)
        val netAmount = qty * rate

        _state.update { currentState ->
            currentState.copy(
                items = currentState.items.map { item ->
                    if (item.purchaseOrderItemUuid != action.purchaseOrderItemUuid) return@map item
                    val taxAmount = netAmount * item.taxPercentage / 100.0
                    item.copy(
                        requiredQty = qty,
                        pricePerUnit = rate,
                        netAmount = netAmount,
                        taxAmount = taxAmount,
                        grossAmount = netAmount + taxAmount,
                    )
                }
            )
        }
    }

    private fun saveGrn(deliveryChallanNo: String, deliveryChallanDate: LocalDateTime) {
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
            baseCurrencyCode = "AED",
            deliveryChallanNo = deliveryChallanNo,
            deliveryChallanDate = deliveryChallanDate
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

