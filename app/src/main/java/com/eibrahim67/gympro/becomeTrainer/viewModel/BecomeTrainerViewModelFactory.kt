package com.eibrahim67.gympro.becomeTrainer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepository

class BecomeTrainerViewModelFactory (
    private val remoteRepository: RemoteRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(BecomeTrainerViewModel::class.java))
                return BecomeTrainerViewModel(remoteRepository) as T
            else
                throw IllegalArgumentException("Unknown view model")
        }
    }