package com.eibrahim67.gympro.showTrainPlan.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShowTrainPlanViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowTrainPlanViewModel::class.java))
            return ShowTrainPlanViewModel() as T
        else
            throw IllegalArgumentException("Unknown view model")
    }
}