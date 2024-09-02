package com.eibrahim67.gympro.mainActivity.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse
import com.eibrahim67.gympro.home.model.Coach
import com.eibrahim67.gympro.home.model.Exercise
import com.eibrahim67.gympro.home.model.TrainPlan
import com.eibrahim67.gympro.home.model.Workout

class MainViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _navigateToFragment = MutableLiveData<Int>()
    val navigateToFragment: LiveData<Int> get() = _navigateToFragment

    fun navigateTo(fragmentName: Int) {
        _navigateToFragment.value = fragmentName
    }

    private val _myTrainPlans = MutableLiveData<Response<TrainPlan>>()

    val myTrainPlan: LiveData<Response<TrainPlan>> get() = _myTrainPlans

    fun getMyTrainPlan() {
        applyResponse(_myTrainPlans, viewModelScope) { setTrainPlan() }
    }

    private val _trainPlans = MutableLiveData<Response<List<TrainPlan>>>()

    val trainPlans: LiveData<Response<List<TrainPlan>>> get() = _trainPlans

    fun getTrainPlans() {
        applyResponse(_trainPlans, viewModelScope) { setTrainPlans() }
    }

    private fun setTrainPlans(): List<TrainPlan> = listOf(
        TrainPlan(
            id = 1,
            name = "Full Body Strength Training",
            description = "A comprehensive workout targeting all major muscle groups.",
            durationDaysPerWeek = "5 days per week",
            workoutsList = setWorks(),
            targetedMuscleGroups = listOf("Chest", "Legs", "Back", "Shoulders", "Arms"),
            coachId = 123,
            difficultyLevel = "Intermediate",
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzr3YQCFB8-y9_fpIEkFOLLyie0GOs_J_IBAkkrzUpeX4PiDZANiPLJE4&s=10",
            trainingCategories = listOf()
        )
    )

    private fun setTrainPlan() = TrainPlan(
        id = 1,
        name = "Full Body Strength Training",
        description = "A comprehensive workout targeting all major muscle groups.",
        durationDaysPerWeek = "5 days per week",
        workoutsList = setWorks(),
        targetedMuscleGroups = listOf("Chest", "Legs", "Back", "Shoulders", "Arms"),
        coachId = 123,
        difficultyLevel = "Intermediate",
        imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzr3YQCFB8-y9_fpIEkFOLLyie0GOs_J_IBAkkrzUpeX4PiDZANiPLJE4&s=10",
        trainingCategories = listOf()
    )

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