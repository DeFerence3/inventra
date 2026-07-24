package com.deference.inventra.domain.model.stock.receipt


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("id")
    val id: Int,
    @SerialName("uuid")
    val uuid: String,
    @SerialName("itemId")
    val itemId: Int,
    @SerialName("itemCode")
    val itemCode: String,
    @SerialName("itemName")
    val itemName: String,
    @SerialName("unitId")
    val unitId: Int,
    @SerialName("unitName")
    val unitName: String,
    @SerialName("baseUnitId")
    val baseUnitId: Int,
    @SerialName("baseUnitName")
    val baseUnitName: String,
    @SerialName("storeUnitId")
    val storeUnitId: Int,
    @SerialName("storeUnitName")
    val storeUnitName: String,
    @SerialName("requestedQty")
    val requestedQty: Double,
    @SerialName("issueQty")
    val issueQty: Double,
    @SerialName("conversionFactor")
    val conversionFactor: Double,
    @SerialName("conversionFactorBUToSU")
    val conversionFactorBUToSU: Double,
    @SerialName("qtyMet")
    val qtyMet: Double,
    @SerialName("averagePrice")
    val averagePrice: Double,
    @SerialName("lastPurchasePrice")
    val lastPurchasePrice: Double,
    @SerialName("total")
    val total: Double,
    @SerialName("qtyInBaseUnit")
    val qtyInBaseUnit: Double,
    @SerialName("stockOnHand")
    val stockOnHand: Double,
    @SerialName("notes")
    val notes: String,
    @SerialName("overGroupId")
    val overGroupId: Int,
    @SerialName("overGroupName")
    val overGroupName: String,
    @SerialName("majorGroupId")
    val majorGroupId: Int,
    @SerialName("majorGroupName")
    val majorGroupName: String,
    @SerialName("itemGroupId")
    val itemGroupId: Int,
    @SerialName("itemGroupName")
    val itemGroupName: String,
    @SerialName("authorisationLevel")
    val authorisationLevel: Int,
    @SerialName("authorisedCount")
    val authorisedCount: Int
)