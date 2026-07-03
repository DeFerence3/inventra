package com.deference.inventra.core

import android.content.Intent
import com.deference.inventra.Inventra
import com.deference.inventra.data.prefs.InventraPreference
import com.deference.inventra.data.prefs.InventraPreferenceKeys.BEARER
import com.deference.inventra.data.prefs.InventraPreferenceKeys.EMAIL
import com.deference.inventra.data.prefs.InventraPreferenceKeys.IS_LOGGED_IN
import com.deference.inventra.data.prefs.InventraPreferenceKeys.ROLE
import com.deference.inventra.data.prefs.InventraPreferenceKeys.USERNAME
import com.deference.inventra.domain.model.auth.LoginResponse
import com.deference.inventra.presentation.MainActivity


object Session {

    lateinit var preferences: InventraPreference

    fun init(preferences: InventraPreference) {
        this.preferences = preferences
    }

    var isAppInitializing: Boolean = true

    val isLoggedIn: Boolean
        get() = (
                preferences.getBoolean(IS_LOGGED_IN)
                ).also {
                isAppInitializing = false
            }

    val username: String
        get() = (
                preferences.getString(USERNAME) ?: ""
                )

    val bearer: String
        get() = (
                preferences.getString(BEARER) ?: ""
                )

    val role: String
        get() = preferences.getString(ROLE) ?: ""

    fun setUserData(
        userData: LoginResponse,
    ) {
        preferences.save(USERNAME, userData.name)
        preferences.save(BEARER, userData.token)
        preferences.save(EMAIL, userData.email)
        preferences.save(ROLE, userData.role ?: "")
        preferences.save(IS_LOGGED_IN, true)
    }

    fun logout() {
        preferences.remove(USERNAME)
        preferences.remove(BEARER)
        preferences.remove(IS_LOGGED_IN)
        preferences.remove(EMAIL)
        preferences.remove(ROLE)

        val intent = Intent(Inventra.applicationContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        Inventra.applicationContext().startActivity(intent)
    }
}
