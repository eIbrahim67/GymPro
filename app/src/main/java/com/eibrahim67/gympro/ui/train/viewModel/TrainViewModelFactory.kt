package com.eibrahim67.gympro.ui.train.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.domain.repository.RemoteRepository
import com.eibrahim67.gympro.domain.repository.UserRepository

class TrainViewModelFactory(

    private val userRepository: UserRepository, private val remoteRepository: RemoteRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainViewModel::class.java)) return TrainViewModel(
            userRepository,
            remoteRepository
        ) as T
        else throw IllegalArgumentException("Unknown view model")
    }

}