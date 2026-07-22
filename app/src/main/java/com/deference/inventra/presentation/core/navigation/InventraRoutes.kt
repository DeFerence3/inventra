package com.deference.inventra.presentation.core.navigation

import androidx.navigation3.runtime.NavKey
import com.deference.inventra.domain.model.approvals.ApprovalRequestType
import kotlinx.serialization.Serializable

sealed interface InventraRoutes: NavKey {
    @Serializable
    data object Login : InventraRoutes
    @Serializable
    data object Home : InventraRoutes
    @Serializable
    data object SupplierList : InventraRoutes
    @Serializable
    data class PurchaseOrderList(val supplierId: Int,val supplierName: String) : InventraRoutes
    @Serializable
    data class ItemList(val poUUIDs: List<String>?,val supplierId: Int,val supplierName: String, val scannedBillJson: String? = null) : InventraRoutes
    @Serializable
    data object ApprovalList : InventraRoutes
    @Serializable
    data class Approve(val type: ApprovalRequestType, val approvalId: String, val transUuId: List<String>): InventraRoutes
    @Serializable
    data object PurchaseRequest : InventraRoutes
    @Serializable
    data object SpotCheck : InventraRoutes
    @Serializable
    data object Scan : InventraRoutes
    @Serializable
    data object StockRequest : InventraRoutes
}
