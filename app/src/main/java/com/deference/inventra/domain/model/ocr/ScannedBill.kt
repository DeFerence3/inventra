package com.deference.inventra.domain.model.ocr

import kotlinx.serialization.Serializable

@Serializable
data class ScannedBill(
    val supplierName: String,
    val invoiceNo: String,
    val date: String,
    val items: List<ScannedBillItem>,
    val totalAmount: Double
)

@Serializable
data class ScannedBillItem(
    val name: String,
    val qty: Double,
    val rate: Double,
    val tax: Double,
    val gross: Double
)
