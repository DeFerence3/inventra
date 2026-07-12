package com.deference.inventra.domain.repository

import com.deference.inventra.domain.model.Paginated
import com.deference.inventra.domain.model.master.Location
import com.deference.inventra.domain.model.master.Supplier
import com.deference.inventra.domain.model.master.Unit
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface MasterRepo {
    fun searchSupplier(
        name: String?,
        page: Int,
        pageSize: Int
    ): Deferred<Response<Paginated<Supplier>>>

    fun getLocations(
        name: String?,
        page: Int,
        pageSize: Int
    ): Deferred<Response<List<Location>>>

    fun getUnits(
        baseUnitId: Int,
        page: Int,
        pageSize: Int
    ): Deferred<Response<Paginated<Unit>>>
}
