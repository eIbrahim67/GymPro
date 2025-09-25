package com.eibrahim67.gympro.ui.myWorkouts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.data.remote.model.Workout
import com.eibrahim67.gympro.domain.repository.RemoteRepository
import com.eibrahim67.gympro.domain.repository.UserRepository
import com.eibrahim67.gympro.utils.UtilsFunctions.applyResponse
import com.eibrahim67.gympro.utils.response.ResponseEI
import com.eibrahim67.gympro.data.local.model.User


class MyWorkoutsViewModel(
    private val remoteRepository: RemoteRepository, private val userRepository: UserRepository
) : ViewModel() {

    private val _loggedInUser = MutableLiveData<ResponseEI<User?>>()
    val loggedInUser: LiveData<ResponseEI<User?>> get() = _loggedInUser

    fun getLoggedInUser() {
        applyResponse(_loggedInUser, viewModelScope) {
            userRepository.getLoggedInUser()
        }
    }

    private val _myWorkoutsIds = MutableLiveData<ResponseEI<List<Int>?>>()
    val myWorkoutsIds: LiveData<ResponseEI<List<Int>?>> get() = _myWorkoutsIds

    fun getMyWorkoutsIds(id: String) {
        applyResponse(_myWorkoutsIds, viewModelScope) {
            remoteRepository.getMyWorkoutsIds(id)
        }
    }

    private val _workouts = MutableLiveData<ResponseEI<List<Workout>?>>()
    val workouts: LiveData<ResponseEI<List<Workout>?>> get() = _workouts

    fun getWorkouts(ids: List<Int>) {
        applyResponse(_workouts, viewModelScope) {
            remoteRepository.getWorkoutsByIds(ids)
        }
    }

    private val _deleteWorkout = MutableLiveData<ResponseEI<Unit>>()
    val deleteWorkout: LiveData<ResponseEI<Unit>> get() = _deleteWorkout

    fun deleteWorkout(id: Int) {
        applyResponse(_deleteWorkout, viewModelScope) {
            remoteRepository.deleteWorkout(id)
        }
    }

}