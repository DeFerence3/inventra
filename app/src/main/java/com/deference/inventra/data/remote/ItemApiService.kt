package com.deference.inventra.data.remote

import com.deference.inventra.domain.model.Paginated
import com.deference.inventra.domain.model.item.SearchItem
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ItemApiService{
    @GET("api/master/purchaseitem/union-paged")
    fun getItemList(
        @Query("searchKey") name: String?,
        @Query("page") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Deferred<Response<Paginated<SearchItem>>>
}