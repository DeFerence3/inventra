package com.deference.inventra.presentation.purchaserequest

import com.deference.inventra.domain.model.item.SearchItem
import com.deference.inventra.domain.model.master.Location
import com.deference.inventra.domain.model.master.Unit
import kotlinx.datetime.LocalDateTime

sealed interface PurchaseRequestActions {
    data class OnRequestNoChanged(val no: String) : PurchaseRequestActions
    data class OnRequestDateChanged(val date: LocalDateTime) : PurchaseRequestActions
    data class OnInternalNotesChanged(val notes: String) : PurchaseRequestActions
    data class OnItemExpectedDateChanged(val index: Int, val date: LocalDateTime) : PurchaseRequestActions
    data class OnItemQtyChanged(val index: Int, val qty: String) : PurchaseRequestActions
    data class OnItemRemarksChanged(val index: Int, val remarks: String) : PurchaseRequestActions
    data class SelectLocation(val index: Int, val location: Location) : PurchaseRequestActions
    data class SelectItem(val index: Int, val item: SearchItem) : PurchaseRequestActions
    data class SelectUnit(val index: Int, val unit: Unit) : PurchaseRequestActions
    data object AddItem : PurchaseRequestActions
    data class RemoveItem(val index: Int) : PurchaseRequestActions
    data object Submit : PurchaseRequestActions
}
