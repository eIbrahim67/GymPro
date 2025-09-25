package com.eibrahim67.gympro.ui.becomeTrainer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.domain.repository.RemoteRepository

class BecomeTrainerViewModelFactory(
    private val remoteRepository: RemoteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BecomeTrainerViewModel::class.java)) return BecomeTrainerViewModel(
            remoteRepository
        ) as T
        else throw IllegalArgumentException("Unknown view model")
    }
}