package com.eibrahim67.gympro.train.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepository

class TrainViewModelFactory(

    private val userRepository: UserRepository,
    private val remoteRepository: RemoteRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainViewModel::class.java))
            return TrainViewModel(userRepository, remoteRepository) as T
        else
            throw IllegalArgumentException("Unknown view model")
    }

}