package com.deference.inventra.presentation.spotcheck

import kotlinx.datetime.LocalDateTime

sealed interface SpotCheckActions {
    data class OnSpotCheckNoChanged(val no: String) : SpotCheckActions
    data class OnSpotCheckDateChanged(val date: LocalDateTime) : SpotCheckActions
    data class OnInternalNotesChanged(val notes: String) : SpotCheckActions

    data class OnItemQtyChanged(val index: Int, val qty: String) : SpotCheckActions
    data class OnItemRemarksChanged(val index: Int, val remarks: String) : SpotCheckActions
    
    data object AddItem : SpotCheckActions
    data class RemoveItem(val index: Int) : SpotCheckActions
    data object Submit : SpotCheckActions

    data class OpenItemSelector(val index: Int) : SpotCheckActions
    data object CloseItemSelector : SpotCheckActions
    data class OpenLocationSelector(val index: Int) : SpotCheckActions
    data object CloseLocationSelector : SpotCheckActions
    data class OnItemSearchQueryChanged(val query: String) : SpotCheckActions
    data class OnLocationSearchQueryChanged(val query: String) : SpotCheckActions
    data class SelectItem(val index: Int, val item: com.deference.inventra.domain.model.item.SearchItem) : SpotCheckActions
    data class SelectLocation(val index: Int, val location: String) : SpotCheckActions
}
