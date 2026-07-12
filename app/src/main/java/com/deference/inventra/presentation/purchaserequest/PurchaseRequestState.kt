package com.deference.inventra.presentation.purchaserequest

import com.deference.inventra.core.utils.now
import com.deference.inventra.domain.model.item.SearchItem
import com.deference.inventra.domain.model.master.Location
import com.deference.inventra.domain.model.master.Unit
import kotlinx.datetime.LocalDateTime

data class PurchaseRequestState(
    val requestNo: String = "",
    val requestDate: LocalDateTime = LocalDateTime.now(),
    val internalNotes: String = "",
    val items: List<PurchaseRequestItemInput> = listOf(PurchaseRequestItemInput()),
    val isLoading: Boolean = false,
    val isSubmitted: Boolean = false,
    val error: String? = null
)

data class PurchaseRequestItemInput(
    val item: SearchItem? = null,
    val location: Location? = null,
    val unit: Unit? = null,
    val units: List<Unit> = emptyList(),
    val expectedDate: LocalDateTime = LocalDateTime.now(),
    val requestedQty: String = "1",
    val remarks: String = "",
    val locationError: String? = null,
    val itemError: String? = null,
    val unitError: String? = null,
    val requestedQtyError: String? = null
) {
    val itemCode: String = item?.itemCode ?: ""
    val itemName: String = item?.itemName ?: ""
    val itemGroup: String = item?.itemGroup ?: ""
    val unitName: String = unit?.name ?: item?.baseUnit ?: ""
}
