package com.deference.inventra.domain.model.grn


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("requiredQty")
    val requiredQty: Double? = null,
    @SerialName("baseUnitId")
    val baseUnitId: Int,
    @SerialName("baseUnitName")
    val baseUnitName: String? = null,
    @SerialName("pricePerUnit")
    val pricePerUnit: Double,
    @SerialName("pricePerBaseUnit")
    val pricePerBaseUnit: Double,
    @SerialName("locationId")
    val locationId: Int,
    @SerialName("locationName")
    val locationName: String? = null,
    @SerialName("remarks")
    val remarks: String? = null,
    @SerialName("requisitionId")
    val requisitionId: Int? = null,
    @SerialName("requisitionItemId")
    val requisitionItemId: Int? = null,
    @SerialName("receivedQty")
    val receivedQty: Double? = null,
    @SerialName("grnExternalId")
    val grnExternalId: Int? = null,
    @SerialName("grnItemExternalId")
    val grnItemExternalId: Int? = null,
    @SerialName("taxPercentage")
    val taxPercentage: Double,
    @SerialName("netAmount")
    val netAmount: Double? = null,
    @SerialName("taxAmount")
    val taxAmount: Double? = null,
    @SerialName("grossAmount")
    val grossAmount: Double? = null,
    @SerialName("itemId")
    val itemId: Int? = null,
    @SerialName("itemName")
    val itemName: String? = null,
    @SerialName("itemCode")
    val itemCode: String? = null,
    @SerialName("unitId")
    val unitId: Int,
    @SerialName("unitName")
    val unitName: String? = null,
    @SerialName("pendingPOQty")
    val pendingPOQty: Double? = null,
    @SerialName("offerQty")
    val offerQty: Double? = null,
    @SerialName("expectedDate")
    val expectedDate: String? = null,
    @SerialName("deliveryDate")
    val deliveryDate: String? = null,
    @SerialName("poTransID")
    val poTransID: Int? = null,
    @SerialName("poID")
    val poID: Int? = null,
    @SerialName("invoiceQtyMet")
    val invoiceQtyMet: Boolean? = null,
    @SerialName("discPercentage")
    val discPercentage: Double? = null,
    @SerialName("discAmount")
    val discAmount: Double? = null,
    @SerialName("vatPercentage")
    val vatPercentage: Double? = null,
    @SerialName("vatAmount")
    val vatAmount: Double? = null,
    @SerialName("qtyInBaseUnit")
    val qtyInBaseUnit: Double? = null,
    @SerialName("poPrice")
    val poPrice: Double? = null,
    @SerialName("itemGroupId")
    val itemGroupId: Int? = null,
    @SerialName("itemGroupName")
    val itemGroupName: String? = null,
    @SerialName("overGroupId")
    val overGroupId: Int? = null,
    @SerialName("overGroupName")
    val overGroupName: String? = null,
    @SerialName("majorGroupId")
    val majorGroupId: Int? = null,
    @SerialName("majorGroupName")
    val majorGroupName: String? = null,
    @SerialName("vendorId")
    val vendorId: Int? = null,
    @SerialName("isHACCPRequired")
    val isHACCPRequired: Boolean? = null,
    @SerialName("expiryDays")
    val expiryDays: Int? = null,
    @SerialName("isStockItem")
    val isStockItem: Boolean? = null,
    @SerialName("storeUnitId")
    val storeUnitId: Int? = null,
    @SerialName("storeUnitName")
    val storeUnitName: String? = null,
    @SerialName("purchaseItemId")
    val purchaseItemId: Int? = null,
    @SerialName("purchaseItemName")
    val purchaseItemName: String? = null,
    @SerialName("purchaseItemCode")
    val purchaseItemCode: String? = null
)