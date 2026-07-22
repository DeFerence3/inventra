package com.deference.inventra.domain.model.purchase.requisition


import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.deference.inventra.domain.model.purchase.Item as SummaryItem

@Serializable
data class RequestedItem(
    @SerialName("itemCode")
    val itemCode: String,
    @SerialName("itemName")
    val itemName: String,
    @SerialName("unitName")
    val unitName: String,
    @SerialName("locationName")
    val locationName: String,
    @SerialName("requestedQuantity")
    val requestedQuantity: Double,
    @SerialName("stockOnHand")
    val stockOnHand: Double,
    @SerialName("lastPurchasePrice")
    val lastPurchasePrice: Double,
    @SerialName("lastPurchaseDate")
    val lastPurchaseDate: LocalDateTime?,
){
    fun toSummaryItem() = SummaryItem(
        itemId = 0,
        itemCode = itemCode,
        itemName = itemName,
        unitName = unitName,
        baseUnitName = unitName,
        requiredQty = requestedQuantity,
        pricePerUnit = lastPurchasePrice,
        pricePerBaseUnit = lastPurchasePrice,
        grossAmount = 0.0,
        vendorName = "No Vendor",
        locationName = locationName,
        itemGroupName = "",
        overGroupName = "",
        majorGroupName = "",
        stockOnHand = stockOnHand
    )
}