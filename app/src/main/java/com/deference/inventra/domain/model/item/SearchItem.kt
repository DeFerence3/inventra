package com.deference.inventra.domain.model.item


import com.deference.inventra.domain.model.purchase.Item
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchItem(
    @SerialName("itemId")
    val itemId: Int,
    @SerialName("itemName")
    val itemName: String,
    @SerialName("baseUnit")
    val baseUnit: String,
    @SerialName("storeUnit")
    val storeUnit: String,
    @SerialName("itemCode")
    val itemCode: String?
) {

    fun toItem(): Item = Item(
        itemCode = itemCode ?: "",
        itemName = itemName,
        unitName = baseUnit,
        baseUnitName = baseUnit,
        requiredQty = 0.0,
        pricePerUnit = 0.0,
        pricePerBaseUnit = 0.0,
        grossAmount = 0.0,
        vendorName = "",
        locationName = "",
        stockOnHand = 0
    )
}