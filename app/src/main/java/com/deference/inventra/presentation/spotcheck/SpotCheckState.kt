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
    val error: String? = null
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

