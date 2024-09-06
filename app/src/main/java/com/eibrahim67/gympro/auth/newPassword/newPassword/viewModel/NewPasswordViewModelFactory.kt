package com.eibrahim67.gympro.auth.newPassword.newPassword.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.core.data.local.repository.UserRepository

class NewPasswordViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewPasswordViewModel::class.java))
            return NewPasswordViewModel(userRepository) as T
        else
            throw IllegalArgumentException("Unknown view model")
    }
}