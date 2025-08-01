package com.eibrahim67.gympro.core.response

sealed class FailureReason {
    data object NoInternet : FailureReason()
    class UnknownError(val error: String) : FailureReason()
}