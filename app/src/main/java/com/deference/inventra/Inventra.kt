package com.deference.inventra

import android.app.Application
import android.content.Context
import com.deference.inventra.core.Session
import com.deference.inventra.data.prefs.InventraPreference
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

@HiltAndroidApp
class Inventra: Application(){
    @Inject
    lateinit var preferences: InventraPreference

    override fun onCreate() {
        super.onCreate()
        instance = this
        applicationScope = CoroutineScope(SupervisorJob())
        Session.init(preferences)
    }



    companion object {
        private var instance: Inventra? = null
        lateinit var applicationScope: CoroutineScope

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}