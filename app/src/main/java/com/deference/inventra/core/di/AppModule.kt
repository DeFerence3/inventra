package com.deference.inventra.core.di

import android.content.Context
import com.deference.inventra.data.prefs.InventraPreference
import com.deference.inventra.presentation.core.navigation.InventraRoutes
import com.deference.inventra.presentation.core.navigation.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideInventraPreference(@ApplicationContext context: Context): InventraPreference = InventraPreference(context)

    @Provides
    fun provideNavigator(@ApplicationContext context: Context): Navigator = Navigator(InventraRoutes.Home)

}