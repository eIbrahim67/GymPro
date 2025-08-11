package com.eibrahim67.gympro.myTrainingPlans

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.model.User
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepository
import com.eibrahim67.gympro.core.response.ResponseEI
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

    private val _myTrainPlansIds = MutableLiveData<ResponseEI<List<Int>?>>()
    val myTrainPlansIds: LiveData<ResponseEI<List<Int>?>> get() = _myTrainPlansIds

    fun getMyTrainPlansIds(id:Int) {
        applyResponse(_myTrainPlansIds, viewModelScope) {
            remoteRepository.getMyTrainPlansIds(id)
        }
    }

    private val _trainPlans = MutableLiveData<ResponseEI<List<TrainPlan>?>>()
    val trainPlans: LiveData<ResponseEI<List<TrainPlan>?>> get() = _trainPlans

    fun getTrainPlans(ids: List<Int>) {
        applyResponse(_trainPlans, viewModelScope) {
            remoteRepository.getTrainPlanByIds(ids)
        }
    }


}