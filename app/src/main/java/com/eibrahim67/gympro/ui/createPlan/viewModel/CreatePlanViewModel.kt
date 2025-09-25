package com.eibrahim67.gympro.ui.createPlan.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.data.local.model.User
import com.eibrahim67.gympro.data.remote.model.Category
import com.eibrahim67.gympro.data.remote.model.Muscles
import com.eibrahim67.gympro.data.remote.model.TrainPlan
import com.eibrahim67.gympro.data.remote.model.Workout
import com.eibrahim67.gympro.domain.repository.RemoteRepository
import com.eibrahim67.gympro.domain.repository.UserRepository
import com.eibrahim67.gympro.utils.UtilsFunctions.applyResponse
import com.eibrahim67.gympro.utils.response.ResponseEI

class CreatePlanViewModel(
    private val remoteRepository: RemoteRepository, private val userRepository: UserRepository
) : ViewModel() {

    private val _categories = MutableLiveData<ResponseEI<List<Category>>>()
    val categories: LiveData<ResponseEI<List<Category>>> get() = _categories
    fun getAllCategories() {
        applyResponse(_categories, viewModelScope) {
            remoteRepository.getAllCategories()
        }
    }

    private val _muscles = MutableLiveData<ResponseEI<List<Muscles?>>>()
    val muscles: LiveData<ResponseEI<List<Muscles?>>> get() = _muscles
    fun getAllMuscles() {
        applyResponse(_muscles, viewModelScope) {
            remoteRepository.getAllMuscles()
        }
    }

    private val _workouts = MutableLiveData<ResponseEI<List<Workout?>>>()
    val workouts: LiveData<ResponseEI<List<Workout?>>> get() = _workouts

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

    private val _addedTrainPlanId = MutableLiveData<ResponseEI<Unit>>()
    val addedTrainPlanId: LiveData<ResponseEI<Unit>> get() = _addedTrainPlanId

    fun addTrainPlanId(coachId: String, newPlanId: Int) {
        applyResponse(_addedTrainPlanId, viewModelScope) {
            remoteRepository.addTrainPlanId(coachId, newPlanId)
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