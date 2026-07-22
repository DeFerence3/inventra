package com.deference.inventra.domain.repository

import com.deference.inventra.domain.model.Paginated
import com.deference.inventra.domain.model.approvals.ApprovalActionRequest
import com.deference.inventra.domain.model.approvals.ApprovalDetails
import com.deference.inventra.domain.model.approvals.ApprovalItem
import com.deference.inventra.domain.model.approvals.ApprovalRequestType
import com.deference.inventra.domain.model.grn.GrnRequest
import com.deference.inventra.domain.model.pr.PrRequestBody
import com.deference.inventra.domain.model.purchase.ItemSummaryItem
import com.deference.inventra.domain.model.purchase.OrderItem
import com.deference.inventra.domain.model.purchase.PurchaseOrder
import com.deference.inventra.domain.model.purchase.requisition.RequisitionSummaryResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface PurchaseRepo {
    fun getPurchaseOrdersBySupplier(
        name: String?,
        page: Int,
        pageSize: Int,
        supplierId: Int
    ): Deferred<Response<Paginated<PurchaseOrder>>>

    fun getItemsByPurchaseOrders(
        poUUIDs: List<String>
    ): Deferred<Response<List<OrderItem>>>

    fun saveGrn(
        grn: GrnRequest
    ): Deferred<Response<Unit>>

    fun savePurchaseRequisition(
        purchaseRequisition: PrRequestBody
    ): Deferred<Response<Unit>>

    fun getApprovals(
        status: String,
        transType: ApprovalRequestType,
        search: String?,
        isGrouped: Boolean,
        page: Int,
        pageSize: Int,
    ): Deferred<Response<Paginated<ApprovalItem>>>

    fun getApprovalDetails(
        approvalId: String
    ): Deferred<Response<ApprovalDetails>>

    fun performApprovalAction(
        action: ApprovalActionRequest,
        status: String
    ): Deferred<Response<Unit>>

    fun getPurchaseOrderItemsSummary(
        purchaseOrderUUIDs: List<String>
    ): Deferred<Response<List<ItemSummaryItem>>>

    fun getPurchaseRequisitionSummary(
        requisitionUUIDs: List<String>
    ): Deferred<Response<List<RequisitionSummaryResponse>>>

}
