package com.deference.inventra.presentation.login

sealed interface LoginActions {
    data class OnPasswordChanged(val password: String) : LoginActions
    data class OnUsernameChanged(val username: String) : LoginActions
    data object DoLogin : LoginActions
}