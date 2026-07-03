package com.deference.inventra.core.utils.network

sealed class RequestState<out T> {
    data object Loading : RequestState<Nothing>()
    data class Success<out T>(val data: T) : RequestState<T>()
    data class Error(val message: String) : RequestState<Nothing>()
}