package com.eibrahim67.gympro.ui.auth.newPassword.emailVerification.viewModel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eibrahim67.gympro.ui.auth.signup.model.ValidateCredentials

class EmailVerificationViewModel : ViewModel() {

    private val _emailMessage = MutableLiveData<ValidateCredentials>()
    val emailMessage: LiveData<ValidateCredentials> = _emailMessage

    fun validateEmail(email: String) {
        _emailMessage.value = when {
            email.isEmpty() -> ValidateCredentials.InValid("Email cannot be empty")
            !Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() -> ValidateCredentials.InValid("Invalid email address")

            else -> ValidateCredentials.Valid
        }
    }

    fun validateCredentials(
        isEmailValid: Boolean
    ): ValidateCredentials {
        return when {
            !isEmailValid -> {

                ValidateCredentials.Valid
            }

            else -> {
                ValidateCredentials.InValid("Please write your email")
            }
        }
    }

}