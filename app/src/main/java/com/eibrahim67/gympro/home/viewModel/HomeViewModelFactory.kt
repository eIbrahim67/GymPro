package com.eibrahim67.gympro.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepository

class HomeViewModelFactory(
    private val remoteRepository: RemoteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(remoteRepository) as T
        else
            throw IllegalArgumentException("Unknown view model")
    }
}