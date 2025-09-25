package com.eibrahim67.gympro.ui.train.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.data.remote.model.TrainPlan
import com.eibrahim67.gympro.domain.repository.RemoteRepository
import com.eibrahim67.gympro.domain.repository.UserRepository
import com.eibrahim67.gympro.utils.UtilsFunctions.applyResponse
import com.eibrahim67.gympro.utils.response.ResponseEI

class TrainViewModel(
    private val userRepository: UserRepository,
    private val remoteRepository: RemoteRepository
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
                ?.let { remoteRepository.getTrainPlanById(it) }
        }
    }
}