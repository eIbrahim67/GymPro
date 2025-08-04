package com.eibrahim67.gympro.createPlan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.model.User
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.remote.model.Category
import com.eibrahim67.gympro.core.data.remote.model.Exercise
import com.eibrahim67.gympro.core.data.remote.model.Muscles
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.model.Workout
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepository
import com.eibrahim67.gympro.core.data.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse

class CreatePlanViewModel(
    private val remoteRepository: RemoteRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _categories = MutableLiveData<ResponseEI<Map<Int, Category>>>()

    val categories: LiveData<ResponseEI<Map<Int, Category>>> get() = _categories

    fun getAllCategories() {
        applyResponse(_categories, viewModelScope) {
            remoteRepository.getAllCategories()
        }
    }

    private val _muscles = MutableLiveData<ResponseEI<Map<Int, Muscles?>>>()
    val muscles: LiveData<ResponseEI<Map<Int, Muscles?>>> get() = _muscles
    fun getAllMuscles() {
        applyResponse(_muscles, viewModelScope) {
            remoteRepository.getAllMuscles()
        }
    }

    private val _exercises = MutableLiveData<ResponseEI<Map<Int, Exercise?>>>()

    val exercises: LiveData<ResponseEI<Map<Int, Exercise?>>> get() = _exercises

    fun getAllExercises() {
        applyResponse(_exercises, viewModelScope) {
            remoteRepository.getAllExercises()
        }
    }

    private val _workouts = MutableLiveData<ResponseEI<Map<Int, Workout?>>>()
    val workouts: LiveData<ResponseEI<Map<Int, Workout?>>> get() = _workouts

    fun getAllWorkouts() {
        applyResponse(_workouts, viewModelScope) {
            remoteRepository.getAllWorkouts()
        }
    }

    private val _createPlan = MutableLiveData<ResponseEI<Unit>>()
    val createPlan: LiveData<ResponseEI<Unit>> get() = _createPlan

    fun createPlan(trainPlan: TrainPlan) {
        applyResponse(_createPlan, viewModelScope) {
            remoteRepository.addTrainPlans(trainPlan)
        }
    }

    private val _loggedInUser = MutableLiveData<ResponseEI<User?>>()
    val loggedInUser: LiveData<ResponseEI<User?>> get() = _loggedInUser

    fun getLoggedInUser() {
        applyResponse(_loggedInUser, viewModelScope) {
            userRepository.getLoggedInUser()
        }
    }

}