package com.eibrahim67.gympro.createPlan.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepository

class CreatePlanViewModelFactory(
    private val remoteRepository: RemoteRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CreatePlanViewModel::class.java))
            return CreatePlanViewModel(remoteRepository, userRepository) as T
        else
            throw IllegalArgumentException("Unknown view model")
    }
}