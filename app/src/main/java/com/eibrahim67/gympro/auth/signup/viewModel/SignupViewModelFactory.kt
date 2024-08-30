package com.eibrahim67.gympro.auth.signup.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.core.data.local.repository.UserRepository

class SignupViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignupViewModel::class.java))
            return SignupViewModel(userRepository) as T
        else
            throw IllegalArgumentException("Unknown view model")
    }
}