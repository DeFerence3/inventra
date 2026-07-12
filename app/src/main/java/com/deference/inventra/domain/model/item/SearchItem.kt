package com.deference.inventra.domain.model.item

import com.deference.inventra.domain.model.purchase.Item
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serializable as JavaSerializable

@Serializable
data class SearchItem(
    @SerialName("itemId")
    val itemId: Int,
    @SerialName("itemName")
    val itemName: String,
    @SerialName("baseUnitId")
    val baseUnitId: Int,
    @SerialName("baseUnit")
    val baseUnit: String,
    @SerialName("storeUnit")
    val storeUnit: String,
    @SerialName("itemGroup")
    val itemGroup: String,
    @SerialName("overGroup")
    val overGroup: String,
    @SerialName("majorGroup")
    val majorGroup: String,
    @SerialName("itemCode")
    val itemCode: String?
) : JavaSerializable {

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
        stockOnHand = 0,
        itemGroupName = itemGroup,
        overGroupName = overGroup,
        majorGroupName = majorGroup,
    )
}
