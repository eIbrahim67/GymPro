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
import com.eibrahim67.gympro.core.data.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse

class AddDataViewModel(private val remoteRepository: RemoteRepository) : ViewModel() {

    private val _addCategoryResponseEI = MutableLiveData<ResponseEI<Unit>>()
    val addCategoryResponseEI: LiveData<ResponseEI<Unit>> get() = _addCategoryResponseEI
    fun addCategory(category: Map<Int, Category>) =
        applyResponse(_addCategoryResponseEI, viewModelScope) {
            remoteRepository.addCategory(category)
        }

    private val _addMuscleResponseEI = MutableLiveData<ResponseEI<Unit>>()
    val addMuscleResponseEI: LiveData<ResponseEI<Unit>> get() = _addMuscleResponseEI
    fun addMuscle(muscles: Map<Int, Muscles>) =
        applyResponse(_addMuscleResponseEI, viewModelScope) {
            remoteRepository.addMuscle(muscles)
        }

    private val _addCoachResponseEI = MutableLiveData<ResponseEI<Unit>>()
    val addCoachResponseEI: LiveData<ResponseEI<Unit>> get() = _addMuscleResponseEI
    fun addCoach(coach: Map<Int, Coach>) =
        applyResponse(_addCoachResponseEI, viewModelScope) {
            remoteRepository.addCoach(coach)
        }

    private val _addExerciseResponseEI = MutableLiveData<ResponseEI<Unit>>()
    val addExerciseResponseEI: LiveData<ResponseEI<Unit>> get() = _addExerciseResponseEI
    fun addExercise(exercise: Map<Int, Exercise>) =
        applyResponse(_addCoachResponseEI, viewModelScope) {
            remoteRepository.addExercise(exercise)
        }

    private val _addWorkoutResponseEI = MutableLiveData<ResponseEI<Unit>>()
    val addWorkoutResponseEI: LiveData<ResponseEI<Unit>> get() = _addWorkoutResponseEI
    fun addWorkout(workout: Map<Int, Workout>) =
        applyResponse(_addCoachResponseEI, viewModelScope) {
            remoteRepository.addWorkout(workout)
        }

    private val _addTrainPlanResponseEI = MutableLiveData<ResponseEI<Unit>>()
    val addTrainPlanResponseEI: LiveData<ResponseEI<Unit>> get() = _addTrainPlanResponseEI
    fun addTrainPlan(trainPlan: Map<Int, TrainPlan>) =
        applyResponse(_addCoachResponseEI, viewModelScope) {
            remoteRepository.addTrainPlan(trainPlan)
        }

}