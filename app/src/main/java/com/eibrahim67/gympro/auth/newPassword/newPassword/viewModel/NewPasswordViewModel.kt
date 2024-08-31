package com.eibrahim67.gympro.auth.newPassword.newPassword.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.auth.signup.model.ValidateCredentials
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse

class NewPasswordViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _password = MutableLiveData<Response<Unit>>()
    val password: LiveData<Response<Unit>> = _password

    private val _passwordMessage = MutableLiveData<ValidateCredentials>()
    val passwordMessage: LiveData<ValidateCredentials> = _passwordMessage

    private val _confirmPasswordMessage = MutableLiveData<ValidateCredentials>()
    val confirmPasswordMessage: LiveData<ValidateCredentials> = _confirmPasswordMessage

    fun validatePasswordConfirmation(password: String, confirmPassword: String) {
        _confirmPasswordMessage.value = when {
            confirmPassword.isEmpty() -> ValidateCredentials.InValid("Confirmation cannot be empty")
            confirmPassword != password -> ValidateCredentials.InValid("Passwords do not match")

            else -> ValidateCredentials.Valid
        }
    }

    fun updatePassword(email: String, password: String) = applyResponse(_password, viewModelScope) {
        userRepository.updatePassword(email, password)
    }

    fun validatePassword(password: String) {
        _passwordMessage.value = when {
            password.isEmpty() -> ValidateCredentials.InValid("Password cannot be empty")
            password.length < 8 -> ValidateCredentials.InValid("Minimum 8 characters long")
            !password.matches(".*[0-9].*".toRegex()) -> ValidateCredentials.InValid("Minimum one number")
            !password.matches(".*[A-Z].*".toRegex()) -> ValidateCredentials.InValid("Minimum one uppercase letter")
            !password.matches(".*[a-z].*".toRegex()) -> ValidateCredentials.InValid("Minimum one lowercase letter")

            else -> ValidateCredentials.Valid
        }
    }

    fun validateCredentials(
        isPasswordValid: Boolean,
        isConfirmPasswordValid: Boolean
    ): ValidateCredentials {
        return when {
            !isPasswordValid && !isConfirmPasswordValid
            -> {

                ValidateCredentials.Valid
            }

            else -> {
                ValidateCredentials.InValid("Please fill in all fields")
            }
        }
    }

}