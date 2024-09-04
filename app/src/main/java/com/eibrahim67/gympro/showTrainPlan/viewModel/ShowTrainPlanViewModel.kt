package com.eibrahim67.gympro.showTrainPlan.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.data.writtenData.model.TrainPlan
import com.eibrahim67.gympro.core.data.writtenData.source.SourceWrittenData
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse

class ShowTrainPlanViewModel : ViewModel() {

    private val _trainingPlan = MutableLiveData<Response<TrainPlan?>>()

    val trainPlan: LiveData<Response<TrainPlan?>> get() = _trainingPlan

    fun getTrainingPlanById(id: Int) = applyResponse(_trainingPlan, viewModelScope) {
        SourceWrittenData.getTrainingPlansById(id)
    }

}