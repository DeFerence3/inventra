package com.deference.inventra.domain.model.purchase

import com.deference.inventra.domain.model.grn.Item
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(
    @SerialName("purchaseOrderId")
    val purchaseOrderId: Int,
    @SerialName("purchaseOrderItemId")
    val purchaseOrderItemId: Int,
    @SerialName("purchaseOrderItemUuid")
    val purchaseOrderItemUuid: String,
    @SerialName("itemId")
    val itemId: Int,
    @SerialName("itemName")
    val itemName: String,
    @SerialName("itemCode")
    val itemCode: String,
    @SerialName("itemGroupId")
    val itemGroupId: Int,
    @SerialName("itemGroupName")
    val itemGroupName: String,
    @SerialName("majorGroupId")
    val majorGroupId: Int,
    @SerialName("majorGroupName")
    val majorGroupName: String,
    @SerialName("overGroupId")
    val overGroupId: Int,
    @SerialName("overGroupName")
    val overGroupName: String,
    @SerialName("unitId")
    val unitId: Int,
    @SerialName("unitName")
    val unitName: String,
    @SerialName("baseUnitId")
    val baseUnitId: Int,
    @SerialName("baseUnitName")
    val baseUnitName: String,
    @SerialName("purchaseItemId")
    val purchaseItemId: Int,
    @SerialName("purchaseItemName")
    val purchaseItemName: String?,
    @SerialName("requiredQty")
    val requiredQty: Double,
    @SerialName("pricePerUnit")
    val pricePerUnit: Double,
    @SerialName("pricePerBaseUnit")
    val pricePerBaseUnit: Double,
    @SerialName("receivedQty")
    val receivedQty: Double,
    @SerialName("remarks")
    val remarks: String?,
    @SerialName("locationId")
    val locationId: Int,
    @SerialName("locationName")
    val locationName: String,
    @SerialName("discountPercentage")
    val discountPercentage: Double,
    @SerialName("discountAmount")
    val discountAmount: Double,
    @SerialName("taxPercentage")
    val taxPercentage: Double,
    @SerialName("taxAmount")
    val taxAmount: Double,
    @SerialName("netAmount")
    val netAmount: Double,
    @SerialName("grossAmount")
    val grossAmount: Double,
    @SerialName("expiryDays")
    val expiryDays: Int?,
    @SerialName("storeUnitId")
    val storeUnitId: Int,
    @SerialName("storeUnitName")
    val storeUnitName: String?,
    @SerialName("isHACCPRequired")
    val isHACCPRequired: Boolean,
    @SerialName("isStockItem")
    val isStockItem: Boolean,
    @SerialName("conversionFactorBUToSU")
    val conversionFactorBUToSU: Double?
){
    fun toItem() = Item(
        itemId = itemId,
        itemName = itemName,
        itemCode = itemCode,
        purchaseItemId = purchaseItemId,
        purchaseItemName = purchaseItemName,
        taxAmount = taxAmount,
        requiredQty = requiredQty,
        receivedQty = receivedQty,
        unitName = unitName,
        netAmount = netAmount,
        grossAmount = grossAmount,
        locationId = locationId,
        pricePerUnit = pricePerUnit,
        baseUnitId = baseUnitId,
        pricePerBaseUnit = pricePerBaseUnit,
        taxPercentage = taxPercentage,
        unitId = unitId,
        locationName = locationName,
        baseUnitName = baseUnitName,
        remarks = remarks,
        pendingPOQty = receivedQty,
        offerQty = 0.0,
        poTransID = purchaseOrderItemId,
        poID = purchaseOrderId,
        isStockItem = isStockItem,
        storeUnitId = storeUnitId,
        storeUnitName = storeUnitName,
        invoiceQtyMet = false,
        discPercentage = discountPercentage,
        discAmount = discountAmount,
        expiryDays = expiryDays,
        vatPercentage = taxPercentage,
        vatAmount = taxAmount,
        itemGroupName = itemGroupName,
        majorGroupName = majorGroupName,
        overGroupName = overGroupName,
        majorGroupId = majorGroupId,
        overGroupId = overGroupId,
        itemGroupId = itemGroupId,
    )
}
