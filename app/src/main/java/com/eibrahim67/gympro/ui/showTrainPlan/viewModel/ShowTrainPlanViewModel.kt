package com.eibrahim67.gympro.ui.showTrainPlan.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.data.remote.model.TrainPlan
import com.eibrahim67.gympro.domain.repository.RemoteRepository
import com.eibrahim67.gympro.utils.UtilsFunctions.applyResponse
import com.eibrahim67.gympro.utils.response.ResponseEI

class ShowTrainPlanViewModel(
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    private val _trainingPlan = MutableLiveData<ResponseEI<TrainPlan?>>()

    val trainPlan: LiveData<ResponseEI<TrainPlan?>> get() = _trainingPlan

    fun getTrainingPlanById(id: Int) = applyResponse(_trainingPlan, viewModelScope) {
        remoteRepository.getTrainPlanById(id)
    }

}