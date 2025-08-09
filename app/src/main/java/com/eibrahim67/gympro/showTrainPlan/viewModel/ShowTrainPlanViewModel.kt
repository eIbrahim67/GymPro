package com.eibrahim67.gympro.showTrainPlan.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.response.ResponseEI
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepository
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse

class ShowTrainPlanViewModel(
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    private val _trainingPlan = MutableLiveData<ResponseEI<TrainPlan?>>()

    val trainPlan: LiveData<ResponseEI<TrainPlan?>> get() = _trainingPlan

    fun getTrainingPlanById(id: Int) = applyResponse(_trainingPlan, viewModelScope) {
        remoteRepository.getTrainPlanById(id)
    }

}