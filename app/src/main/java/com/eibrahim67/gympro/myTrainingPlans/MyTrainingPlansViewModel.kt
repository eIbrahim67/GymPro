package com.eibrahim67.gympro.myTrainingPlans

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.model.User
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.model.Workout
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepository
import com.eibrahim67.gympro.core.data.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse

class MyTrainingPlansViewModel(
    private val remoteRepository: RemoteRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loggedInUser = MutableLiveData<ResponseEI<User?>>()
    val loggedInUser: LiveData<ResponseEI<User?>> get() = _loggedInUser

    fun getLoggedInUser() {
        applyResponse(_loggedInUser, viewModelScope) {
            userRepository.getLoggedInUser()
        }
    }

    private val _myTrainPlans = MutableLiveData<ResponseEI<List<String>?>>()
    val myTrainPlans: LiveData<ResponseEI<List<String>?>> get() = _myTrainPlans

    fun getMyTrainPlans(id:Int) {
        applyResponse(_myTrainPlans, viewModelScope) {
            remoteRepository.getMyTrainPlans(id)
        }
    }

    private val _trainPlanById = MutableLiveData<ResponseEI<TrainPlan?>>()
    val trainPlanById: LiveData<ResponseEI<TrainPlan?>> get() = _trainPlanById

    fun getTrainPlanById(id: Int) {
        applyResponse(_trainPlanById, viewModelScope) {
            remoteRepository.getTrainPlanById(id)
        }
    }

}