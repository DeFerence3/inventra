package com.deference.inventra.domain.model.stock


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockReceiptRequestItem(
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
    @SerialName("conversionFactor")
    val conversionFactor: Double,
    @SerialName("conversionFactorBUToSU")
    val conversionFactorBUToSU: Double,
    @SerialName("issuedQty")
    val issuedQty: Double,
    @SerialName("receiptQty")
    val receiptQty: Double,
    @SerialName("averagePrice")
    val averagePrice: Double,
    @SerialName("lastPurchasePrice")
    val lastPurchasePrice: Double,
    @SerialName("total")
    val total: Double,
    @SerialName("qtyInBaseUnit")
    val qtyInBaseUnit: Int,
    @SerialName("notes")
    val notes: String,
    @SerialName("itemGroupId")
    val itemGroupId: Int,
    @SerialName("itemGroupName")
    val itemGroupName: String,
    @SerialName("overGroupId")
    val overGroupId: Int,
    @SerialName("overGroupName")
    val overGroupName: String,
    @SerialName("majorGroupId")
    val majorGroupId: Int,
    @SerialName("majorGroupName")
    val majorGroupName: String
)