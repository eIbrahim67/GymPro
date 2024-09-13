package com.eibrahim67.gympro.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.remote.model.Category
import com.eibrahim67.gympro.core.data.remote.model.Coach
import com.eibrahim67.gympro.core.data.remote.model.Exercise
import com.eibrahim67.gympro.core.data.remote.model.Workout
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepository
import com.eibrahim67.gympro.core.data.response.FailureReason
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.data.writtenData.source.SourceWrittenData
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeViewModel(
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    private val _categories = MutableLiveData<Response<List<Category>?>>()

    val categories: LiveData<Response<List<Category>?>> get() = _categories

//    fun getCategories() = applyResponse(_categories, viewModelScope) {
//        remoteRepository.getCategories().value
//    }

//    private val _categories = MutableLiveData<List<Category>>()
//    val categories: LiveData<List<Category>> get() = _categories

    fun getCategories() {
        _categories.value = Response.Loading
        try {
            viewModelScope.launch {
                remoteRepository.getCategories().observeForever { fetchedCategories ->
                    _categories.value = Response.Success(fetchedCategories)
                }
            }
        } catch (e: Exception) {
            _categories.value = Response.Failure(FailureReason.UnknownError(e.message.toString()))
        }
    }


    private val _exercises = MutableLiveData<Response<List<Exercise>>>()

    val exercises: LiveData<Response<List<Exercise>>> get() = _exercises

    fun getExercises() =
        applyResponse(_exercises, viewModelScope) { SourceWrittenData.getExercisesData() }

    private val _workouts = MutableLiveData<Response<List<Workout>>>()

    val workouts: LiveData<Response<List<Workout>>> get() = _workouts

    fun getWorkouts() =
        applyResponse(_workouts, viewModelScope) { SourceWrittenData.getWorkoutsData() }

    private val _coaches = MutableLiveData<Response<List<Coach>>>()

    val coaches: LiveData<Response<List<Coach>>> get() = _coaches

    fun getCoaches() =
        applyResponse(_coaches, viewModelScope) { SourceWrittenData.getCoachesData() }

    private val _currentDate = MutableLiveData<Response<String>>()
    val currentDate: LiveData<Response<String>> get() = _currentDate
    fun getCurrentDate() = applyResponse(_currentDate, viewModelScope) {
        SimpleDateFormat("EEEE, d MMMM", Locale.ENGLISH).format(Date())
    }

    private val _helloSate = MutableLiveData<Response<String>>()
    val helloSate: LiveData<Response<String>> get() = _helloSate
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
