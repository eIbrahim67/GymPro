package com.eibrahim67.gympro.ui.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.data.remote.model.Coach
import com.eibrahim67.gympro.data.remote.model.TrainPlan
import com.eibrahim67.gympro.domain.repository.RemoteRepository
import com.eibrahim67.gympro.domain.repository.UserRepository
import com.eibrahim67.gympro.utils.UtilsFunctions.applyResponse
import com.eibrahim67.gympro.utils.response.ResponseEI
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeViewModel(
    private val userRepository: UserRepository,
    private val remoteRepository: RemoteRepository,
) : ViewModel() {

    private val _myTrainPlans = MutableLiveData<ResponseEI<TrainPlan?>>()
    val myTrainPlan: LiveData<ResponseEI<TrainPlan?>> get() = _myTrainPlans
    fun getMyTrainPlan() {
        applyResponse(_myTrainPlans, viewModelScope) {
            userRepository.getUserTrainPlanId()?.let { remoteRepository.getTrainPlanById(it) }
        }
    }

    private val _coaches = MutableLiveData<ResponseEI<List<Coach>>>()
    val coaches: LiveData<ResponseEI<List<Coach>>> get() = _coaches
    fun getAllCoaches() =
        applyResponse(_coaches, viewModelScope) { remoteRepository.getAllCoaches() }

    private val _currentDate = MutableLiveData<ResponseEI<String>>()
    val currentDate: LiveData<ResponseEI<String>> get() = _currentDate
    fun getCurrentDate() = applyResponse(_currentDate, viewModelScope) {
        SimpleDateFormat("EEEE, d MMMM", Locale.ENGLISH).format(Date())
    }

    private val _helloSate = MutableLiveData<ResponseEI<String>>()
    val helloSate: LiveData<ResponseEI<String>> get() = _helloSate
    fun getHelloSate() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        applyResponse(_helloSate, viewModelScope) {
            when (hour) {
                in 4..11 -> "Good morning"
                in 12..17 -> "Good afternoon"
                else -> "Good evening"
            }
        }
    }
}
