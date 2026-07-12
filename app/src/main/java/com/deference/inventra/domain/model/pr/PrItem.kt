package com.deference.inventra.domain.model.pr


import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrItem(
    @SerialName("itemId")
    val itemId: Int,
    @SerialName("itemName")
    val itemName: String,
    @SerialName("itemCode")
    val itemCode: String,
    @SerialName("unitId")
    val unitId: Int,
    @SerialName("unitName")
    val unitName: String,
    @SerialName("baseUnitId")
    val baseUnitId: Int,
    @SerialName("baseUnitName")
    val baseUnitName: String,
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
    val majorGroupName: String,
    @SerialName("vendorId")
    val vendorId: Int,
    @SerialName("vendorName")
    val vendorName: String,
    @SerialName("vendorGroupId")
    val vendorGroupId: Int,
    @SerialName("vendorGroupName")
    val vendorGroupName: String,
    @SerialName("requestedQuantity")
    val requestedQuantity: Int,
    @SerialName("expectedDate")
    val expectedDate: LocalDateTime,
    @SerialName("remarks")
    val remarks: String,
    @SerialName("locationId")
    val locationId: Int,
    @SerialName("locationName")
    val locationName: String,
    @SerialName("stockOnHand")
    val stockOnHand: Int,
    @SerialName("stockOnOrder")
    val stockOnOrder: Int,
    @SerialName("lastPurchaseDate")
    val lastPurchaseDate: LocalDateTime?,
    @SerialName("lastPurchasePrice")
    val lastPurchasePrice: Int,
    @SerialName("netAmountLP")
    val netAmountLP: Int,
    @SerialName("vatPercentage")
    val vatPercentage: Int,
    @SerialName("vatAmountLP")
    val vatAmountLP: Int,
    @SerialName("grossAmountLP")
    val grossAmountLP: Int,
    @SerialName("pricePerUnit")
    val pricePerUnit: Int,
    @SerialName("pricePerBaseUnit")
    val pricePerBaseUnit: Int,
    @SerialName("quantityInBaseUnit")
    val quantityInBaseUnit: Int,
    @SerialName("conversionFactor")
    val conversionFactor: Int,
    @SerialName("discountPercentage")
    val discountPercentage: Int,
    @SerialName("discountAmount")
    val discountAmount: Int,
    @SerialName("netAmount")
    val netAmount: Int,
    @SerialName("vatAmount")
    val vatAmount: Int,
    @SerialName("grossAmount")
    val grossAmount: Int,
    @SerialName("authorisationLevel")
    val authorisationLevel: Int,
    @SerialName("isHACCPRequired")
    val isHACCPRequired: Boolean,
    @SerialName("expiryDays")
    val expiryDays: Int,
    @SerialName("isStockItem")
    val isStockItem: Boolean,
    @SerialName("conversionFactorBUToSU")
    val conversionFactorBUToSU: Int,
    @SerialName("rateNumberId")
    val rateNumberId: Int,
    @SerialName("storeUnitId")
    val storeUnitId: Int,
    @SerialName("storeUnitName")
    val storeUnitName: String,
    @SerialName("purchaseItemId")
    val purchaseItemId: Int,
    @SerialName("purchaseItemName")
    val purchaseItemName: String,
    @SerialName("purchaseItemCode")
    val purchaseItemCode: String
)