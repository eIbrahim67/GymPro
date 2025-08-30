package com.eibrahim67.gympro.createExercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.model.User
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.remote.model.Category
import com.eibrahim67.gympro.core.data.remote.model.Exercise
import com.eibrahim67.gympro.core.data.remote.model.Muscles
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepository
import com.eibrahim67.gympro.core.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse

class CreateExerciseViewModel(
    val remoteRepository: RemoteRepository, private val userRepository: UserRepository
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

    private val _exercises = MutableLiveData<ResponseEI<List<Exercise?>>>()
    val exercises: LiveData<ResponseEI<List<Exercise?>>> get() = _exercises

    fun getAllExercises() {
        applyResponse(_exercises, viewModelScope) {
            remoteRepository.getAllExercises()
        }
    }

    private val _createExercise = MutableLiveData<ResponseEI<Unit>>()
    val createExercise: LiveData<ResponseEI<Unit>> get() = _createExercise

    fun createExercise(exercise: Exercise) {
        applyResponse(_createExercise, viewModelScope) {
            remoteRepository.addExercises(exercise)
        }
    }

    private val _addedExerciseId = MutableLiveData<ResponseEI<Unit>>()
    val addedExerciseId: LiveData<ResponseEI<Unit>> get() = _addedExerciseId

    fun addExerciseId(coachId: Int, newPlanId: Int) {
        applyResponse(_addedExerciseId, viewModelScope) {
            remoteRepository.addExerciseId(coachId, newPlanId)
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