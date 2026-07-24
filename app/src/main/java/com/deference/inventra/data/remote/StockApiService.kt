package com.deference.inventra.data.remote

import com.deference.inventra.domain.model.Paginated
import com.deference.inventra.domain.model.spotcheck.SpotCheckRequestBody
import com.deference.inventra.domain.model.stock.StockReceiptRequest
import com.deference.inventra.domain.model.stock.StockReceiptResponseBody
import com.deference.inventra.domain.model.stock.StockRequestRequestBody
import com.deference.inventra.domain.model.stock.receipt.ReceiptResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface StockApiService {
    @POST("api/stock/StockAdjustment")
    fun spotCheck(@Body body: SpotCheckRequestBody): Deferred<Response<Unit>>

    @POST("api/stock/StockRequest")
    fun saveStockRequest(@Body body: StockRequestRequestBody): Deferred<Response<Unit>>

    @POST("/api/stock/StockReceipt")
    fun saveStockReceipt(@Body body: StockReceiptRequest): Deferred<Response<Unit>>

    @GET("api/stock/StockTransfer/paged")
    fun listStockRequest(
        @Query("Status") status: Int,
        @Query("Page") pageNumber: Int,
        @Query("PageSize") pageSize: Int
    ): Deferred<Response<Paginated<StockReceiptResponseBody>>>

    @GET("api/stock/StockTransfer/{transNo}")
    fun details(
        @Path("transNo") transNo: String
    ): Deferred<Response<ReceiptResponse>>
}