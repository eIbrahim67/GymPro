package com.eibrahim67.gympro.home.viewModel

import android.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.remote.model.Category
import com.eibrahim67.gympro.core.data.remote.model.Coach
import com.eibrahim67.gympro.core.data.remote.model.Exercise
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.model.Workout
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepository
import com.eibrahim67.gympro.core.data.response.FailureReason
import com.eibrahim67.gympro.core.data.response.ResponseEI
import com.eibrahim67.gympro.core.data.writtenData.source.SourceWrittenData
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeViewModel(
    private val remoteRepository: RemoteRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _myTrainPlans = MutableLiveData<ResponseEI<TrainPlan?>>()
    val myTrainPlan: LiveData<ResponseEI<TrainPlan?>> get() = _myTrainPlans
    fun getMyTrainPlan() {
        applyResponse(_myTrainPlans, viewModelScope) {
            userRepository.getUserTrainPlanId()
                ?.let { SourceWrittenData.getTrainingPlansById(it) }
        }
    }

//    private val _categories = MutableLiveData<ResponseEI<List<Category>?>>()
//
//    val categories: LiveData<ResponseEI<List<Category>?>> get() = _categories
//
//    fun getCategories() = applyResponse(_categories, viewModelScope) {
//        remoteRepository.getCategories().value
//    }
//
//    private val _exercises = MutableLiveData<ResponseEI<List<Exercise>>>()
//
//    val exercises: LiveData<ResponseEI<List<Exercise>>> get() = _exercises
//
//    fun getExercises() =
//        applyResponse(_exercises, viewModelScope) { SourceWrittenData.getExercisesData() }
//
//    private val _workouts = MutableLiveData<ResponseEI<List<Workout>>>()
//
//    val workouts: LiveData<ResponseEI<List<Workout>>> get() = _workouts
//
//    fun getWorkouts() =
//        applyResponse(_workouts, viewModelScope) { SourceWrittenData.getWorkoutsData() }

    private val _coaches = MutableLiveData<ResponseEI<List<Coach>>>()
    val coaches: LiveData<ResponseEI<List<Coach>>> get() = _coaches
    fun getCoaches() =
        applyResponse(_coaches, viewModelScope) { SourceWrittenData.getCoachesData() }

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
