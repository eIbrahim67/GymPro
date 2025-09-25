package com.eibrahim67.gympro.ui.showTrainPlan.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.domain.repository.RemoteRepository

class ShowTrainPlanViewModelFactory(
    private val remoteRepository: RemoteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowTrainPlanViewModel::class.java)) return ShowTrainPlanViewModel(
            remoteRepository
        ) as T
        else throw IllegalArgumentException("Unknown view model")
    }
}