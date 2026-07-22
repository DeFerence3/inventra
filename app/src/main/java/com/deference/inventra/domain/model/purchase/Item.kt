package com.deference.inventra.domain.model.purchase


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("itemId")
    val itemId: Int = 0,
    @SerialName("itemCode")
    val itemCode: String,
    @SerialName("itemName")
    val itemName: String,
    @SerialName("unitName")
    val unitName: String,
    @SerialName("baseUnitName")
    val baseUnitName: String,
    @SerialName("requiredQty")
    val requiredQty: Double,
    @SerialName("pricePerUnit")
    val pricePerUnit: Double,
    @SerialName("pricePerBaseUnit")
    val pricePerBaseUnit: Double,
    @SerialName("grossAmount")
    val grossAmount: Double,
    @SerialName("vendorName")
    val vendorName: String,
    @SerialName("locationName")
    val locationName: String,
    @SerialName("itemGroupName")
    val itemGroupName: String?,
    @SerialName("overGroupName")
    val overGroupName: String?,
    @SerialName("majorGroupName")
    val majorGroupName: String?,
    @SerialName("stockOnHand")
    val stockOnHand: Double
)