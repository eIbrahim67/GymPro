package com.eibrahim67.gympro.core.main.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.domain.repository.RemoteRepository
import com.eibrahim67.gympro.domain.repository.UserRepository

class MainViewModelFactory(
    private val userRepository: UserRepository,
    private val remoteRepository: RemoteRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(userRepository, remoteRepository) as T
        else
            throw IllegalArgumentException("Unknown view model")
    }

}