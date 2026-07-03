package com.deference.inventra.data.repository

import com.deference.inventra.data.remote.PurchaseApiService
import com.deference.inventra.domain.model.Paginated
import com.deference.inventra.domain.model.approvals.ApprovalActionRequest
import com.deference.inventra.domain.model.approvals.ApprovalDetails
import com.deference.inventra.domain.model.approvals.ApprovalItem
import com.deference.inventra.domain.model.grn.GrnRequest
import com.deference.inventra.domain.model.purchase.ItemSummaryItem
import com.deference.inventra.domain.model.purchase.OrderItem
import com.deference.inventra.domain.model.purchase.PurchaseOrder
import com.deference.inventra.domain.repository.PurchaseRepo
import kotlinx.coroutines.Deferred
import retrofit2.Response
import javax.inject.Inject

class PurchaseRepoImpl @Inject constructor(
    private val apiService: PurchaseApiService
) : PurchaseRepo {
    override fun getPurchaseOrdersBySupplier(
        name: String?,
        page: Int,
        pageSize: Int,
        supplierId: Int
    ): Deferred<Response<Paginated<PurchaseOrder>>> {
        return apiService.getPurchaseOrdersBySupplier(
            name = name,
            pageNumber = page,
            pageSize = pageSize,
            supplierId = supplierId
        )
    }

    override fun getItemsByPurchaseOrders(poUUIDs: List<String>): Deferred<Response<List<OrderItem>>> {
        return apiService.getItemsByPurchaseOrders(
            poUUIDs
        )
    }

    override fun saveGrn(grn: GrnRequest): Deferred<Response<Unit>> {
        return apiService.saveGrn(grn)
    }

    override fun getApprovals(
        status: String,
        transType: String,
        search: String?,
        isGrouped: Boolean,
        page: Int,
        pageSize: Int,
    ): Deferred<Response<Paginated<ApprovalItem>>> {
        return apiService.getApprovals(
            status = status,
            transType = transType.takeIf { it != "All" },
            isGrouped = isGrouped,
            pageSize = pageSize,
            pageNumber = page,
            search = search
        )
    }

    override fun getApprovalDetails(approvalId: String): Deferred<Response<ApprovalDetails>> {
        return apiService.getApprovalDetails(approvalId)
    }

    override fun performApprovalAction(action: ApprovalActionRequest,status: String): Deferred<Response<Unit>> {
        return apiService.performApprovalAction(action,status)
    }

    override fun getPurchaseOrderItemsSummary(purchaseOrderUUIDs: List<String>): Deferred<Response<List<ItemSummaryItem>>> {
        return apiService.getPurchaseOrderItemsSummary(purchaseOrderUUIDs)
    }
}