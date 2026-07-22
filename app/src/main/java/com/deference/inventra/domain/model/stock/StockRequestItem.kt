package com.deference.inventra.domain.model.stock

import com.deference.inventra.domain.model.item.SearchItem
import com.deference.inventra.domain.model.master.Unit
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockRequestItem(
    @SerialName("itemId")
    val itemId: Int,
    @SerialName("itemName")
    val itemName: String,
    @SerialName("itemCode")
    val itemCode: String?,
    @SerialName("unitId")
    val unitId: Int,
    @SerialName("unitName")
    val unitName: String,
    @SerialName("itemGroupId")
    val itemGroupId: Int,
    @SerialName("overGroupId")
    val overGroupId: Int,
    @SerialName("majorGroupId")
    val majorGroupId: Int,
    @SerialName("itemGroupName")
    val itemGroupName: String,
    @SerialName("overGroupName")
    val overGroupName: String,
    @SerialName("majorGroupName")
    val majorGroupName: String,
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
    @SerialName("notes")
    val notes: String,
    @SerialName("averagePrice")
    val averagePrice: Double,
    @SerialName("lastPurchasePrice")
    val lastPurchasePrice: Double,
    @SerialName("total")
    val total: Double,
    @SerialName("stockOnHand")
    val stockOnHand: Double,
    @SerialName("qtyInBaseUnit")
    val qtyInBaseUnit: Int,
    @SerialName("conversionFactor")
    val conversionFactor: Int,
    @SerialName("conversionFactorBUToSU")
    val conversionFactorBUToSU: Int,
    @SerialName("authorisationLevel")
    val authorisationLevel: Int
){
    companion object{
        fun fromSearchItem(
            searchItem: SearchItem,
            unit: Unit,
            qty: Double,
            notes: String,
            averagePrice: Double,
            lastPurchasePrice: Double,
            total: Double,
            stockOnHand: Double,
        ) = StockRequestItem(
            itemId = searchItem.itemId,
            itemName = searchItem.itemName,
            itemCode = searchItem.itemCode,
            unitId = unit.id,
            unitName = unit.name,
            itemGroupId = searchItem.itemGroupId,
            overGroupId = searchItem.overGroupId,
            majorGroupId = searchItem.majorGroupId,
            itemGroupName = searchItem.itemGroup,
            overGroupName = searchItem.overGroup,
            majorGroupName = searchItem.majorGroup,
            baseUnitId = searchItem.baseUnitId,
            baseUnitName = searchItem.baseUnit,
            storeUnitId = searchItem.storeUnitId,
            storeUnitName = searchItem.storeUnit,
            requestedQty = qty,
            notes = notes,
            averagePrice = averagePrice,
            lastPurchasePrice = lastPurchasePrice,
            total = total,
            stockOnHand = stockOnHand,
            qtyInBaseUnit = 0,
            conversionFactor = 0,
            conversionFactorBUToSU = 0,
            authorisationLevel = 0
        )
    }
}