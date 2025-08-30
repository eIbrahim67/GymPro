package com.eibrahim67.gympro.createWorkout

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
import com.eibrahim67.gympro.core.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse

class CreateWorkoutViewModel(
    val remoteRepository: RemoteRepository, private val userRepository: UserRepository
) : ViewModel() {

    private val _muscles = MutableLiveData<ResponseEI<List<Muscles?>>>()
    val muscles: LiveData<ResponseEI<List<Muscles?>>> get() = _muscles
    fun getAllMuscles() {
        applyResponse(_muscles, viewModelScope) {
            remoteRepository.getAllMuscles()
        }
    }

    private val _exercises = MutableLiveData<ResponseEI<List<Exercise?>>>()

    val exercises: LiveData<ResponseEI<List<Exercise?>>> get() = _exercises

    fun getAllExercises() {
        applyResponse(_exercises, viewModelScope) {
            remoteRepository.getAllExercises()
        }
    }

    private val _createWorkout = MutableLiveData<ResponseEI<Unit>>()
    val createWorkout: LiveData<ResponseEI<Unit>> get() = _createWorkout

    fun createWorkout(workout: Workout) {
        applyResponse(_createWorkout, viewModelScope) {
            remoteRepository.addWorkouts(workout)
        }
    }

    private val _addedWorkoutId = MutableLiveData<ResponseEI<Unit>>()
    val addedWorkoutId: LiveData<ResponseEI<Unit>> get() = _addedWorkoutId

    fun addWorkoutId(coachId: Int, newPlanId: Int) {
        applyResponse(_addedWorkoutId, viewModelScope) {
            remoteRepository.addWorkoutId(coachId, newPlanId)
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