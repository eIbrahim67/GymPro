package com.eibrahim67.gympro.train.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.response.ResponseEI
import com.eibrahim67.gympro.core.data.writtenData.source.SourceWrittenData
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse

class TrainViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userHaveTrainer = MutableLiveData<ResponseEI<Boolean>>()

    val userHaveTrainer: LiveData<ResponseEI<Boolean>> get() = _userHaveTrainer

    fun isUserHaveTrainer() = applyResponse(
        _userHaveTrainer,
        viewModelScope
    ) { userRepository.isLoggedInUserHaveTrainer() }

    private val _myTrainPlans = MutableLiveData<ResponseEI<TrainPlan?>>()
    val myTrainPlan: LiveData<ResponseEI<TrainPlan?>> get() = _myTrainPlans
    fun getMyTrainPlan() {
        applyResponse(_myTrainPlans, viewModelScope) {
            userRepository.getUserTrainPlanId()
                ?.let { SourceWrittenData.getTrainingPlansById(it) }
        }
    }

}