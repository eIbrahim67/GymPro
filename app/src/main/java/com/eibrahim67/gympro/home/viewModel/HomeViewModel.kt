package com.eibrahim67.gympro.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.data.writtenData.model.Category
import com.eibrahim67.gympro.core.data.writtenData.model.Coach
import com.eibrahim67.gympro.core.data.writtenData.model.Exercise
import com.eibrahim67.gympro.core.data.writtenData.model.Workout
import com.eibrahim67.gympro.core.data.writtenData.source.SourceWrittenData
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse

class HomeViewModel : ViewModel() {

    private val _categories = MutableLiveData<Response<List<Category>>>()

    val categories: LiveData<Response<List<Category>>> get() = _categories

    fun getCategories() = applyResponse(_categories, viewModelScope) {
        SourceWrittenData.getCategoriesData()
    }

    private val _exercises = MutableLiveData<Response<List<Exercise>>>()

    val exercises: LiveData<Response<List<Exercise>>> get() = _exercises

    fun getExercises() =
        applyResponse(_exercises, viewModelScope) { SourceWrittenData.getExercisesData() }

    private val _workouts = MutableLiveData<Response<List<Workout>>>()

    val workouts: LiveData<Response<List<Workout>>> get() = _workouts

    fun getWorkouts() =
        applyResponse(_workouts, viewModelScope) { SourceWrittenData.getWorkoutsData() }

    private val _coaches = MutableLiveData<Response<List<Coach>>>()

    val coaches: LiveData<Response<List<Coach>>> get() = _coaches

    fun getCoaches() =
        applyResponse(_coaches, viewModelScope) { SourceWrittenData.getCoachesData() }

}
