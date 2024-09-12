package com.eibrahim67.gympro.addData.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.remote.model.Category
import com.eibrahim67.gympro.core.data.remote.model.Coach
import com.eibrahim67.gympro.core.data.remote.model.Exercise
import com.eibrahim67.gympro.core.data.remote.model.Muscles
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.model.Workout
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepository
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse

class AddDataViewModel(private val remoteRepository: RemoteRepository) : ViewModel() {

    private val _addCategoryResponse = MutableLiveData<Response<Unit>>()
    val addCategoryResponse: LiveData<Response<Unit>> get() = _addCategoryResponse
    fun addCategory(category: Map<Int, Category>) =
        applyResponse(_addCategoryResponse, viewModelScope) {
            remoteRepository.addCategory(category)
        }

    private val _addMuscleResponse = MutableLiveData<Response<Unit>>()
    val addMuscleResponse: LiveData<Response<Unit>> get() = _addMuscleResponse
    fun addMuscle(muscles: Map<Int, Muscles>) =
        applyResponse(_addMuscleResponse, viewModelScope) {
            remoteRepository.addMuscle(muscles)
        }

    private val _addCoachResponse = MutableLiveData<Response<Unit>>()
    val addCoachResponse: LiveData<Response<Unit>> get() = _addMuscleResponse
    fun addCoach(coach: Map<Int, Coach>) =
        applyResponse(_addCoachResponse, viewModelScope) {
            remoteRepository.addCoach(coach)
        }

    private val _addExerciseResponse = MutableLiveData<Response<Unit>>()
    val addExerciseResponse: LiveData<Response<Unit>> get() = _addExerciseResponse
    fun addExercise(exercise: Map<Int, Exercise>) =
        applyResponse(_addCoachResponse, viewModelScope) {
            remoteRepository.addExercise(exercise)
        }

    private val _addWorkoutResponse = MutableLiveData<Response<Unit>>()
    val addWorkoutResponse: LiveData<Response<Unit>> get() = _addWorkoutResponse
    fun addWorkout(workout: Map<Int, Workout>) =
        applyResponse(_addCoachResponse, viewModelScope) {
            remoteRepository.addWorkout(workout)
        }

    private val _addTrainPlanResponse = MutableLiveData<Response<Unit>>()
    val addTrainPlanResponse: LiveData<Response<Unit>> get() = _addTrainPlanResponse
    fun addTrainPlan(trainPlan: Map<Int, TrainPlan>) =
        applyResponse(_addCoachResponse, viewModelScope) {
            remoteRepository.addTrainPlan(trainPlan)
        }

}