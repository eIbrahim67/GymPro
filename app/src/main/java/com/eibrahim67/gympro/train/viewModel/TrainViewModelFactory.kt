package com.eibrahim67.gympro.train.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.response.FailureReason
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse
import com.eibrahim67.gympro.home.model.Exercise
import com.eibrahim67.gympro.home.model.TrainPlan
import com.eibrahim67.gympro.home.model.Workout

class TrainViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainViewModel::class.java))
            return TrainViewModel(userRepository) as T
        else
            throw IllegalArgumentException("Unknown view model")
    }

}