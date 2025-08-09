package com.eibrahim67.gympro.main.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.remote.model.Coach
import com.eibrahim67.gympro.core.data.remote.model.Exercise
import com.eibrahim67.gympro.core.data.remote.model.Muscles
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.model.Workout
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepository
import com.eibrahim67.gympro.core.data.response.ResponseEI
import com.eibrahim67.gympro.core.utils.Converters
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse


class MainViewModel(
    private val userRepository: UserRepository,
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    private val _navigateRightToFragment = MutableLiveData<Int?>()
    val navigateRightToFragment: LiveData<Int?> get() = _navigateRightToFragment
    fun navigateRightTo(fragmentName: Int?) {
        _navigateRightToFragment.value = fragmentName
    }

    private val _navigateLeftToFragment = MutableLiveData<Int?>()
    val navigateLeftToFragment: LiveData<Int?> get() = _navigateLeftToFragment
    fun navigateLeftTo(fragmentName: Int?) {
        _navigateLeftToFragment.value = fragmentName
    }

    private val _workoutId = MutableLiveData<Int>()
    val workoutId: LiveData<Int> get() = _workoutId
    fun setWorkoutId(id: Int) {
        _workoutId.value = id
    }

    private val _exerciseId = MutableLiveData<Int>()
    val exerciseId: LiveData<Int> get() = _exerciseId
    fun setExerciseId(id: Int) {
        _exerciseId.value = id
    }

    private val _trainPlanId = MutableLiveData<Int>()
    val trainPlanId: LiveData<Int> get() = _trainPlanId
    fun setTrainPlanId(id: Int) {
        _trainPlanId.value = id
    }

    private val _chatWithId = MutableLiveData<Int>()
    val chatWithId: LiveData<Int> get() = _chatWithId
    fun setChatWithId(id: Int) {
        _chatWithId.value = id
    }


    private val _trainPlans = MutableLiveData<ResponseEI<List<TrainPlan>>>()
    val trainPlans: LiveData<ResponseEI<List<TrainPlan>>> get() = _trainPlans
    fun getTrainPlans() {
        applyResponse(_trainPlans, viewModelScope) {
            remoteRepository.getAllTrainPlans()
        }
    }

    private val _coachById = MutableLiveData<ResponseEI<Coach?>>()
    val coachById: LiveData<ResponseEI<Coach?>> get() = _coachById
    fun getCoachById(id: Int) =
        applyResponse(_coachById, viewModelScope) { remoteRepository.getCoachById(id) }

    private val _musclesByIds = MutableLiveData<ResponseEI<List<Muscles>?>>()
    val musclesByIds: LiveData<ResponseEI<List<Muscles>?>> get() = _musclesByIds
    fun getmusclesByIds(ids: List<Int>) = applyResponse(
        _musclesByIds,
        viewModelScope
    ) { remoteRepository.getMusclesByIds(ids) }

    private val _workoutById = MutableLiveData<ResponseEI<Workout?>>()
    val workoutById: LiveData<ResponseEI<Workout?>> get() = _workoutById
    fun getWorkoutById(id: Int) = applyResponse(
        _workoutById,
        viewModelScope
    ) { remoteRepository.getWorkoutById(id) }

    private val _workoutsByIds = MutableLiveData<ResponseEI<List<Workout>?>>()
    val workoutsByIds: LiveData<ResponseEI<List<Workout>?>> get() = _workoutsByIds
    fun getWorkoutsByIds(ids: List<Int>) = applyResponse(
        _workoutsByIds,
        viewModelScope
    ) { remoteRepository.getWorkoutsByIds(ids) }

    private val _exercisesByIds = MutableLiveData<ResponseEI<List<Exercise>?>>()
    val exercisesByIds: LiveData<ResponseEI<List<Exercise>?>> get() = _exercisesByIds
    fun getExerciseByIds(ids: List<Int>) = applyResponse(
        _exercisesByIds,
        viewModelScope
    ) { remoteRepository.getExercisesByIds(ids) }

    private val _exerciseById = MutableLiveData<ResponseEI<Exercise?>>()
    val exerciseById: LiveData<ResponseEI<Exercise?>> get() = _exerciseById
    fun getExerciseById(id: Int) = applyResponse(
        _exerciseById,
        viewModelScope
    ) { remoteRepository.getExerciseById(id) }








    private val _userDataExercise = MutableLiveData<ResponseEI<Map<Int, MutableList<String>>>>()
    val userDataExercise: LiveData<ResponseEI<Map<Int, MutableList<String>>>> get() = _userDataExercise
    fun fetchDateExerciseData() {
        applyResponse(_userDataExercise, viewModelScope) {
            val json = userRepository.getUserExerciseData()
            json?.let { Converters.toMap(it) } ?: emptyMap()
        }
    }

    private val _updateUserExerciseState = MutableLiveData<ResponseEI<Unit>>()
    val updateUserExerciseState: LiveData<ResponseEI<Unit>> get() = _updateUserExerciseState
    fun updateUserExercise(id: Int, weight: String, reps: String) {
        when (val currentExerciseData = _userDataExercise.value) {
            is ResponseEI.Loading -> {
                _updateUserExerciseState.value = ResponseEI.Loading
            }

            is ResponseEI.Success -> {

                val updatedMap: MutableMap<Int, MutableList<String>> =
                    currentExerciseData.data as MutableMap<Int, MutableList<String>>

                updatedMap.getOrPut(id) { mutableListOf() }.add("$weight#$reps")
                _updateUserExerciseState.value = ResponseEI.Success(Unit)
                updateExerciseMap(updatedMap)
            }

            is ResponseEI.Failure -> {
                _updateUserExerciseState.value = ResponseEI.Failure(currentExerciseData.reason)
            }

            null -> {}
        }
    }

    private val _updateUserExercise = MutableLiveData<ResponseEI<Unit>>()
    val updateUserExercise: LiveData<ResponseEI<Unit>> get() = _updateUserExercise
    private fun updateExerciseMap(updatedData: Map<Int, MutableList<String>>) {
        applyResponse(_updateUserExercise, viewModelScope) {
            userRepository.updateUserExerciseData(updatedData)
        }
    }

    private val _updateMyTrainPlan = MutableLiveData<ResponseEI<Unit>>()
    val updateMyTrainPlan: LiveData<ResponseEI<Unit>> get() = _updateMyTrainPlan
    fun updateMyTrainPlan() {
        applyResponse(
            _updateMyTrainPlan,
            viewModelScope
        ) { userRepository.updateTrainPlanId(trainPlanId.value) }
    }

    private val _myCoachState = MutableLiveData<ResponseEI<Unit>>()
    val myCoachState: LiveData<ResponseEI<Unit>> get() = _myCoachState
    fun updateMyCoachState(data: Boolean) {
        applyResponse(
            _myCoachState,
            viewModelScope
        ) { userRepository.updateHaveCoach(data) }
    }
}