package com.eibrahim67.gympro.auth.signin.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.auth.signup.model.ValidateCredentials
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import kotlinx.coroutines.launch

class SigninViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isUserValid = MutableLiveData<ValidateCredentials?>()
    val isUserValid: LiveData<ValidateCredentials?> = _isUserValid

    fun checkUser(email: String, password: String) {
        viewModelScope.launch {
            when (val foundPassword = userRepository.getPassword(email)) {
                null -> _isUserValid.value = ValidateCredentials.InValid("User not found")

                else -> validateUser(password, foundPassword, email)
            }
        }
    }

    private suspend fun validateUser(password: String, realPassword: String, email: String) {

        when (password == realPassword) {
            true -> {
                _isUserValid.value = ValidateCredentials.Valid
                userRepository.logInUser(email)
            }

            false -> {
                _isUserValid.value = ValidateCredentials.InValid("Incorrect Credentials")
            }
        }
    }

    fun resetStates() {
        _isUserValid.value = null
    }

}