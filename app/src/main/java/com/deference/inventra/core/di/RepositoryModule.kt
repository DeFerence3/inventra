package com.deference.inventra.core.di

import com.deference.inventra.data.remote.ApiService
import com.deference.inventra.data.remote.ConfigApiService
import com.deference.inventra.data.remote.PurchaseApiService
import com.deference.inventra.data.remote.StockApiService
import com.deference.inventra.data.repository.AuthRepoImpl
import com.deference.inventra.data.repository.MasterRepoImpl
import com.deference.inventra.data.repository.PurchaseRepoImpl
import com.deference.inventra.data.repository.SpotCheckRepoImpl
import com.deference.inventra.domain.repository.AuthRepo
import com.deference.inventra.domain.repository.MasterRepo
import com.deference.inventra.domain.repository.PurchaseRepo
import com.deference.inventra.domain.repository.SpotCheckRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule{
    @Provides
    @Singleton
    fun provideAuthenticationRepository(apiService: ApiService): AuthRepo {
        return AuthRepoImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideMasterRepository(apiService: ApiService): MasterRepo {
        return MasterRepoImpl(apiService)
    }

    @Provides
    @Singleton
    fun providePurchaseRepository(apiService: PurchaseApiService): PurchaseRepo {
        return PurchaseRepoImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideSpotCheckRepository(apiService: StockApiService,config: ConfigApiService): SpotCheckRepo { return SpotCheckRepoImpl(apiService,config) }
}