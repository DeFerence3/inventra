package com.deference.inventra.data.remote

import com.deference.inventra.core.SortOrder
import com.deference.inventra.domain.model.Paginated
import com.deference.inventra.domain.model.approvals.ApprovalActionRequest
import com.deference.inventra.domain.model.approvals.ApprovalDetails
import com.deference.inventra.domain.model.approvals.ApprovalItem
import com.deference.inventra.domain.model.grn.GrnRequest
import com.deference.inventra.domain.model.purchase.ItemSummaryItem
import com.deference.inventra.domain.model.purchase.OrderItem
import com.deference.inventra.domain.model.purchase.PurchaseOrder
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PurchaseApiService {
    @GET("api/purchase/purchaseorder/ordered-and-partially-received")
    fun getPurchaseOrdersBySupplier(
        @Query("name") name: String?,
        @Query("page") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("sortField") sortField: String = "TransDate",
        @Query("sortOrder") sortOrder: SortOrder = SortOrder.ASC,
        @Query("VendorId") supplierId: Int,
    ): Deferred<Response<Paginated<PurchaseOrder>>>

    @POST("api/purchase/purchaseorder/item-details-by-purchase-order-uuids")
    fun getItemsByPurchaseOrders(
        @Body purchaseOrderUUIDs: List<String>
    ): Deferred<Response<List<OrderItem>>>

    @POST("api/purchase/Grn")
    fun saveGrn(
        @Body grn: GrnRequest
    ): Deferred<Response<Unit>>

    @GET("api/approval/ApprovalInstance/paged")
    fun getApprovals(
        @Query("Status") status: String,
        @Query("transType") transType: String?,
        @Query("SearchKey") search: String?,
        @Query("page") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("sortOrder") sortOrder: SortOrder = SortOrder.ASC,
        @Query("isGrouped") isGrouped: Boolean,
    ): Deferred<Response<Paginated<ApprovalItem>>>

    @GET("api/approval/ApprovalInstance/{approvalId}")
    fun getApprovalDetails(
        @Path("approvalId") approvalId: String
    ): Deferred<Response<ApprovalDetails>>

    @POST("api/purchase/PurchaseOrder/item-summary")
    fun getPurchaseOrderItemsSummary(
        @Body purchaseOrderUUIDs: List<String>
    ): Deferred<Response<List<ItemSummaryItem>>>

    @POST("api/approval/ApprovalAction/{status}")
    fun performApprovalAction(
        @Body action: ApprovalActionRequest,
        @Path("status") status: String
    ): Deferred<Response<Unit>>
}