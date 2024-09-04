package com.eibrahim67.gympro.mainActivity.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.data.writtenData.model.Coach
import com.eibrahim67.gympro.core.data.writtenData.model.Exercise
import com.eibrahim67.gympro.core.data.writtenData.model.TrainPlan
import com.eibrahim67.gympro.core.data.writtenData.model.Workout
import com.eibrahim67.gympro.core.data.writtenData.source.SourceWrittenData
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse


class MainViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _navigateToFragment = MutableLiveData<Int>()
    val navigateToFragment: LiveData<Int> get() = _navigateToFragment

    fun navigateTo(fragmentName: Int) {
        _navigateToFragment.value = fragmentName
    }

    private val _myTrainPlans = MutableLiveData<Response<TrainPlan?>>()

    val myTrainPlan: LiveData<Response<TrainPlan?>> get() = _myTrainPlans

    fun getMyTrainPlan() {
        applyResponse(_myTrainPlans, viewModelScope) { SourceWrittenData.getTrainingPlansById(1) }
        //TODO:Update to be real, get saved data in room
    }

    private val _trainPlans = MutableLiveData<Response<List<TrainPlan>>>()

    val trainPlans: LiveData<Response<List<TrainPlan>>> get() = _trainPlans

    fun getTrainPlans() {
        applyResponse(_trainPlans, viewModelScope) { SourceWrittenData.getTrainingPlansData() }
    }

    private val _trainPlanId = MutableLiveData<Int>()

    val trainPlanId: LiveData<Int> get() = _trainPlanId

    fun setTrainPlanId(id: Int) {
        _trainPlanId.value = id
    }

    private val _workoutId = MutableLiveData<Int>()

    val workoutId: LiveData<Int> get() = _workoutId

    fun setWorkoutId(id: Int) {
        _workoutId.value = id
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


}