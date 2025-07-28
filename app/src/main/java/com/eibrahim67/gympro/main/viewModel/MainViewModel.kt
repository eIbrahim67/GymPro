package com.eibrahim67.gympro.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.remote.model.Coach
import com.eibrahim67.gympro.core.data.remote.model.Exercise
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.model.Workout
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.data.writtenData.source.SourceWrittenData
import com.eibrahim67.gympro.core.utils.Converters
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse


class MainViewModel(
    private val userRepository: UserRepository
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

    private val _myTrainPlans = MutableLiveData<Response<TrainPlan?>>()
    val myTrainPlan: LiveData<Response<TrainPlan?>> get() = _myTrainPlans
    fun getMyTrainPlan() {
        applyResponse(_myTrainPlans, viewModelScope) {
            userRepository.getUserTrainPlanId()
                ?.let { SourceWrittenData.getTrainingPlansById(it) }
        }
    }

    private val _trainPlans = MutableLiveData<Response<List<TrainPlan>>>()
    val trainPlans: LiveData<Response<List<TrainPlan>>> get() = _trainPlans
    fun getTrainPlans() {
        applyResponse(_trainPlans, viewModelScope) { SourceWrittenData.getTrainingPlansData() }
    }

    private val _coachById = MutableLiveData<Response<Coach?>>()
    val coachById: LiveData<Response<Coach?>> get() = _coachById
    fun getCoachById(id: Int) =
        applyResponse(_coachById, viewModelScope) { SourceWrittenData.getCoachById(id) }

    private val _targetedMusclesByIds = MutableLiveData<Response<String>>()
    val targetedMusclesByIds: LiveData<Response<String>> get() = _targetedMusclesByIds
    fun getTargetedMusclesByIds(ids: List<Int>) = applyResponse(
        _targetedMusclesByIds,
        viewModelScope
    ) { SourceWrittenData.getTargetedMusclesByIdsAsString(ids) }

    private val _workoutById = MutableLiveData<Response<Workout?>>()
    val workoutById: LiveData<Response<Workout?>> get() = _workoutById
    fun getWorkoutById(id: Int) = applyResponse(
        _workoutById,
        viewModelScope
    ) { SourceWrittenData.getWorkoutById(id) }

    private val _workoutsByIds = MutableLiveData<Response<List<Workout>?>>()
    val workoutsByIds: LiveData<Response<List<Workout>?>> get() = _workoutsByIds
    fun getWorkoutsByIds(ids: List<Int>) = applyResponse(
        _workoutsByIds,
        viewModelScope
    ) { SourceWrittenData.getWorkoutsByIds(ids) }

    private val _exercisesByIds = MutableLiveData<Response<List<Exercise>?>>()
    val exercisesByIds: LiveData<Response<List<Exercise>?>> get() = _exercisesByIds
    fun getExerciseByIds(ids: List<Int>) = applyResponse(
        _exercisesByIds,
        viewModelScope
    ) { SourceWrittenData.getExercisesByIds(ids) }

    private val _exerciseById = MutableLiveData<Response<Exercise?>>()
    val exerciseById: LiveData<Response<Exercise?>> get() = _exerciseById
    fun getExerciseById(id: Int) = applyResponse(
        _exerciseById,
        viewModelScope
    ) { SourceWrittenData.getExerciseById(id) }

    private val _userDataExercise = MutableLiveData<Response<Map<Int, MutableList<String>>>>()
    val userDataExercise: LiveData<Response<Map<Int, MutableList<String>>>> get() = _userDataExercise
    fun fetchDateExerciseData() {
        applyResponse(_userDataExercise, viewModelScope) {
            val json = userRepository.getUserExerciseData()
            json?.let { Converters.toMap(it) } ?: emptyMap()
        }
    }

    private val _updateUserExerciseState = MutableLiveData<Response<Unit>>()
    val updateUserExerciseState: LiveData<Response<Unit>> get() = _updateUserExerciseState
    fun updateUserExercise(id: Int, weight: String, reps: String) {
        when (val currentExerciseData = _userDataExercise.value) {
            is Response.Loading -> {
                _updateUserExerciseState.value = Response.Loading
            }

            is Response.Success -> {

                val updatedMap: MutableMap<Int, MutableList<String>> =
                    currentExerciseData.data as MutableMap<Int, MutableList<String>>

                updatedMap.getOrPut(id) { mutableListOf() }.add("$weight#$reps")
                _updateUserExerciseState.value = Response.Success(Unit)
                updateExerciseMap(updatedMap)
            }

            is Response.Failure -> {
                _updateUserExerciseState.value = Response.Failure(currentExerciseData.reason)
            }

            null -> {}
        }
    }

    private val _updateUserExercise = MutableLiveData<Response<Unit>>()
    val updateUserExercise: LiveData<Response<Unit>> get() = _updateUserExercise
    private fun updateExerciseMap(updatedData: Map<Int, MutableList<String>>) {
        applyResponse(_updateUserExercise, viewModelScope) {
            userRepository.updateUserExerciseData(updatedData)
        }
    }

    private val _updateMyTrainPlan = MutableLiveData<Response<Unit>>()

    //val updateMyTrainPlan: LiveData<Response<Unit>> get() = _updateMyTrainPlan
    fun updateMyTrainPlan() {
        applyResponse(
            _updateMyTrainPlan,
            viewModelScope
        ) { userRepository.updateTrainPlanId(trainPlanId.value) }
    }

    private val _myCoachState = MutableLiveData<Response<Unit>>()

    //val myCoachState: LiveData<Response<Unit>> get() = _myCoachState
    fun updateMyCoachState(data: Boolean) {
        applyResponse(
            _myCoachState,
            viewModelScope
        ) { userRepository.updateHaveCoach(data) }
    }
}