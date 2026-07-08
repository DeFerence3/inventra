package com.deference.inventra.presentation.spotcheck

import com.deference.inventra.core.utils.now
import com.deference.inventra.domain.model.item.SearchItem
import com.deference.inventra.domain.model.master.Location
import kotlinx.datetime.LocalDateTime

data class SpotCheckState(
    val spotCheckNo: String = "",
    val spotCheckDate: LocalDateTime = LocalDateTime.now(),
    val internalNotes: String = "",
    val items: List<SpotCheckItemInput> = emptyList(),
    val isLoading: Boolean = false,
    val isSubmitted: Boolean = false,
    val error: String? = null,
    val showItemSelectorForIndex: Int? = null,
    val showLocationSelectorForIndex: Int? = null,
    val searchedItems: List<SearchItem> = emptyList(),
    val isSearchingItems: Boolean = false,
    val itemSearchQuery: String = "",
    val locations: List<Location> = emptyList(),
    val isSearchingLocations: Boolean = false,
    val locationSearchQuery: String = ""
)

data class SpotCheckItemInput(
    val item: SearchItem? = null,
    val location: String = "",
    val physicalQty: String = "",
    val remarks: String = ""
){
    val itemCode: String = item?.itemCode ?: ""
    val itemName: String = item?.itemName ?: ""
}
