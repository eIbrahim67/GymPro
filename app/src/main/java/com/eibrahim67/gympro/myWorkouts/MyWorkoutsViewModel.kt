package com.eibrahim67.gympro.myWorkouts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.model.User
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.remote.model.Workout
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepository
import com.eibrahim67.gympro.core.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse

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

    fun getMyWorkoutsIds(id:Int) {
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

}