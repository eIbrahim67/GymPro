package com.eibrahim67.gympro.mainActivity.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.core.data.local.repository.UserRepository

class MainViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(userRepository) as T
        else
            throw IllegalArgumentException("Unknown view model")
    }

}