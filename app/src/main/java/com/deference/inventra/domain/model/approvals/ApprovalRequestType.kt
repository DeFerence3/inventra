package com.deference.inventra.domain.model.approvals

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ApprovalRequestType {
    @SerialName("All")
    ALL,
    @SerialName("PurchaseRequisition")
    PURCHASE_REQUISITION,
    @SerialName("PurchaseOrderItem")
    PURCHASE_ORDER_ITEM,
    @SerialName("StockRequest")
    STOCK_REQUEST;

    val apiValue: String
        get() = serializer().descriptor.getElementName(ordinal)

    val readableName: String
        get() = name.replace("_", " ")
            .lowercase()
            .replaceFirstChar(Char::uppercase)
}
