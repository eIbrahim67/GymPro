package com.eibrahim67.gympro.myExercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.model.User
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.remote.model.Exercise
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepository
import com.eibrahim67.gympro.core.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse

class MyExercisesViewModel(
    private val remoteRepository: RemoteRepository, private val userRepository: UserRepository
) : ViewModel() {

    private val _loggedInUser = MutableLiveData<ResponseEI<User?>>()
    val loggedInUser: LiveData<ResponseEI<User?>> get() = _loggedInUser

    fun getLoggedInUser() {
        applyResponse(_loggedInUser, viewModelScope) {
            userRepository.getLoggedInUser()
        }
    }

    private val _myExercisesIds = MutableLiveData<ResponseEI<List<Int>?>>()
    val myExercisesIds: LiveData<ResponseEI<List<Int>?>> get() = _myExercisesIds

    fun getMyExercisesIds(id:Int) {
        applyResponse(_myExercisesIds, viewModelScope) {
            remoteRepository.getMyExercisesIds(id)
        }
    }

    private val _exercises = MutableLiveData<ResponseEI<List<Exercise>?>>()
    val exercises: LiveData<ResponseEI<List<Exercise>?>> get() = _exercises

    fun getExercises(ids: List<Int>) {
        applyResponse(_exercises, viewModelScope) {
            remoteRepository.getExercisesByIds(ids)
        }
    }

    private val _deleteExercise = MutableLiveData<ResponseEI<Unit>>()
    val deleteExercise: LiveData<ResponseEI<Unit>> get() = _deleteExercise

    fun deleteExercise(id: Int) {
        applyResponse(_deleteExercise, viewModelScope) {
            remoteRepository.deleteExercise(id)
        }

    }

}
