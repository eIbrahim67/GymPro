package com.eibrahim67.gympro.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse
import com.eibrahim67.gympro.home.model.Coach
import com.eibrahim67.gympro.home.model.Exercise
import com.eibrahim67.gympro.home.model.TrainingCategory
import com.eibrahim67.gympro.home.model.Workout

class HomeViewModel : ViewModel() {

    private val _categories = MutableLiveData<Response<List<TrainingCategory>>>()

    val categories: LiveData<Response<List<TrainingCategory>>> get() = _categories

    fun getCategories() {

        applyResponse(_categories, viewModelScope) {
            setCate()
        }

    }

    private fun setCate() = listOf(
        TrainingCategory(
            id = 1,
            name = "Strength",
            description = "Focus on building muscle strength with weights and resistance.",
            iconUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzr3YQCFB8-y9_fpIEkFOLLyie0GOs_J_IBAkkrzUpeX4PiDZANiPLJE4&s=10"
        )
    )

    private val _exercises = MutableLiveData<Response<List<Exercise>>>()

    val exercises: LiveData<Response<List<Exercise>>> get() = _exercises

    fun getExercises() = applyResponse(_exercises, viewModelScope) { setExes() }

    private val _workouts = MutableLiveData<Response<List<Workout>>>()

    val workouts: LiveData<Response<List<Workout>>> get() = _workouts

    fun getWorkouts() = applyResponse(_workouts, viewModelScope) { setWorks() }

    private val _coaches = MutableLiveData<Response<List<Coach>>>()

    val coaches: LiveData<Response<List<Coach>>> get() = _coaches

    fun getCoaches() = applyResponse(_coaches, viewModelScope) { setCoaches() }

    private fun setExes() = listOf(
        Exercise(
            id = 1,
            name = "Incline Bench Press",
            description = "A chest upper exercise that works the chest, front shoulders.",
            exerciseSet = 3,
            categoryIds = 1,
            equipmentNeeded = listOf("Dumbbells", "Bench"),
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzr3YQCFB8-y9_fpIEkFOLLyie0GOs_J_IBAkkrzUpeX4PiDZANiPLJE4&s=10",
            videoUrl = null,
            exerciseReps = 8,
            exerciseHint = "Dumbbell"
        )
    )

    private fun setWorks() = listOf(
        Workout(
            id = 1,
            name = "Chest press and Triceps",
            description = "null",
            durationMinutes = 120,
            exercises = setExes(),
            targetedMuscleGroups = listOf("Chest", "Front Shoulder", "Triceps"),
            coachId = 1,
            difficultyLevel = "Intermediate",
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzr3YQCFB8-y9_fpIEkFOLLyie0GOs_J_IBAkkrzUpeX4PiDZANiPLJE4&s=10",
            null
        )

    )

    private fun setCoaches(): List<Coach> = listOf(
        Coach(
            id = 1,
            name = "Ibrahim Mohamed",
            specialization = "Strength",
            experienceYears = 1,
            certifications = listOf(),
            bio = "I have over a year of experience in training and researching muscle growth techniques to effectively increase muscle mass and overall fitness.",
            profileImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzr3YQCFB8-y9_fpIEkFOLLyie0GOs_J_IBAkkrzUpeX4PiDZANiPLJE4&s=10",
            contactEmail = "ibrahim.mohamed.coach@gmail.com",
            contactPhone = "01550162282",
            price = 250.00,
            rate = 4.6
        )

    )
}
