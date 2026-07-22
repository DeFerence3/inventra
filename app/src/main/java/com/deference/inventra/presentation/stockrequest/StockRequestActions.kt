package com.deference.inventra.presentation.stockrequest

import com.deference.inventra.domain.model.item.SearchItem
import com.deference.inventra.domain.model.master.Location
import com.deference.inventra.domain.model.master.Unit

sealed interface StockRequestActions {
    data class SelectReceiptLocation(val location: Location) : StockRequestActions
    data class SelectTransferLocation(val location: Location) : StockRequestActions
    data class OnRemarksChanged(val remarks: String) : StockRequestActions
    
    data class SelectItem(val index: Int, val item: SearchItem) : StockRequestActions
    data class SelectUnit(val index: Int, val unit: Unit) : StockRequestActions
    data class OnItemQtyChanged(val index: Int, val qty: String) : StockRequestActions
    data class OnItemRemarksChanged(val index: Int, val remarks: String) : StockRequestActions
    
    data object AddItem : StockRequestActions
    data class RemoveItem(val index: Int) : StockRequestActions
    
    data object Submit : StockRequestActions
}
