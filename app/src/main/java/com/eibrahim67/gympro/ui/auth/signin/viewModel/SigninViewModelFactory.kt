package com.eibrahim67.gympro.ui.auth.signin.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.domain.repository.UserRepository

class SigninViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SigninViewModel::class.java))
            return SigninViewModel(userRepository) as T
        throw IllegalArgumentException("Unknown view model")
    }
}