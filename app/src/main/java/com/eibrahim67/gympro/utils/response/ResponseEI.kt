package com.eibrahim67.gympro.utils.response

sealed class ResponseEI<out T> {
    data object Loading : ResponseEI<Nothing>()
    data class Success<out T>(val data: T) : ResponseEI<T>()
    data class Failure(val reason: FailureReason) : ResponseEI<Nothing>()
}