package com.deference.inventra.data.repository

import com.deference.inventra.data.remote.ApiService
import com.deference.inventra.domain.model.Paginated
import com.deference.inventra.domain.model.master.Location
import com.deference.inventra.domain.model.master.Supplier
import com.deference.inventra.domain.repository.MasterRepo
import kotlinx.coroutines.Deferred
import retrofit2.Response
import javax.inject.Inject

class MasterRepoImpl @Inject constructor(
    private val apiService: ApiService
) : MasterRepo {
    override fun searchSupplier(
        name: String?,
        page: Int,
        pageSize: Int
    ): Deferred<Response<Paginated<Supplier>>> {
        return apiService.searchSupplier(name, page, pageSize)
    }

    override fun getLocations(
        name: String?,
        page: Int,
        pageSize: Int
    ): Deferred<Response<List<Location>>> {
        return apiService.locations(name, page, pageSize)
    }
}
