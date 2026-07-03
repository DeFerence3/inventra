package com.deference.inventra.presentation.login

data class LoginState(
    val username: String = "",
    val password: String = "",
    val isUsernameError: Boolean = false,
    val isPasswordError: Boolean = false,
    val isLoading: Boolean = false
)