package com.deference.inventra.domain.model.item

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
    @SerialName("storeUnitId")
    val storeUnitId: Int,
    @SerialName("storeUnit")
    val storeUnit: String,
    @SerialName("itemGroupId")
    val itemGroupId: Int,
    @SerialName("itemGroup")
    val itemGroup: String,
    @SerialName("overGroupId")
    val overGroupId: Int,
    @SerialName("overGroup")
    val overGroup: String,
    @SerialName("majorGroupId")
    val majorGroupId: Int,
    @SerialName("majorGroup")
    val majorGroup: String,
    @SerialName("itemCode")
    val itemCode: String?
) : JavaSerializable