package com.eibrahim67.gympro.auth.signup.model

sealed class ValidateCredentials {
    data object Valid : ValidateCredentials()
    data class InValid(val message: String) : ValidateCredentials()
}