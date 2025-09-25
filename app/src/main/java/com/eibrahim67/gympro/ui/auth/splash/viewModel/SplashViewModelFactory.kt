package com.eibrahim67.gympro.ui.auth.splash.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.domain.repository.UserRepository

class SplashViewModelFactory(

    private val userRepository: UserRepository

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java))
            return SplashViewModel(userRepository) as T

        throw IllegalArgumentException("Unknown view model")
    }

}