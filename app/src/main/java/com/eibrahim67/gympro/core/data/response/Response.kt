package com.eibrahim67.gympro.core.data.response

sealed class Response<out T> {
    data object Loading : Response<Nothing>()
    data class Success<out T>(val data: T) : Response<T>()
    data class Failure(val reason: FailureReason) : Response<Nothing>()
}

