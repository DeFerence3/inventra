package com.deference.inventra.presentation.stockrequest

import com.deference.inventra.core.utils.now
import com.deference.inventra.domain.model.item.SearchItem
import com.deference.inventra.domain.model.master.Location
import com.deference.inventra.domain.model.master.Unit
import kotlinx.datetime.LocalDateTime

data class StockRequestState(
    val requestNo: String = "",
    val requestDate: LocalDateTime = LocalDateTime.now(),
    val receiptLocation: Location? = null,
    val transferLocation: Location? = null,
    val remarks: String = "",
    val items: List<StockRequestItemInput> = listOf(StockRequestItemInput()),
    val isLoading: Boolean = false,
    val isSubmitted: Boolean = false,
    val error: String? = null,
    
    val receiptLocationError: String? = null,
    val transferLocationError: String? = null
)

data class StockRequestItemInput(
    val item: SearchItem? = null,
    val unit: Unit? = null,
    val units: List<Unit> = emptyList(),
    val requestedQty: String = "1.00",
    val remarks: String = "",
    val itemError: String? = null,
    val unitError: String? = null,
    val requestedQtyError: String? = null
) {
    val itemCode: String = item?.itemCode ?: ""
    val itemName: String = item?.itemName ?: ""
    val itemGroup: String = item?.itemGroup ?: ""
    val unitName: String = unit?.name ?: item?.baseUnit ?: ""
}
