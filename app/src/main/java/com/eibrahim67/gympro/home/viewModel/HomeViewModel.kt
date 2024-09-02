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
            iconUrl = "https://example.com/icons/strength.png"
        ),
        TrainingCategory(
            id = 1,
            name = "Strength",
            description = "Focus on building muscle strength with weights and resistance.",
            iconUrl = "https://example.com/icons/strength.png"
        ),
        TrainingCategory(
            id = 1,
            name = "Strength",
            description = "Focus on building muscle strength with weights and resistance.",
            iconUrl = "https://example.com/icons/strength.png"
        )
    )

    private val _exercises = MutableLiveData<Response<List<Exercise>>>()

    val exercises: LiveData<Response<List<Exercise>>> get() = _exercises

    fun getExercises() = applyResponse(_exercises, viewModelScope) { setExes() }

    private fun setExes() = listOf(
        Exercise(
            id = 1,
            name = "Push-ups",
            description = "A basic upper body strength exercise that works the chest, shoulders, and triceps.",
            durationMinutes = 5,
            exerciseSet = 3,
            category = "Strength",
            equipmentNeeded = listOf("None"),
            imageUrl = "https://example.com/images/pushups.png",
            videoUrl = null,
            exerciseReps = 10,
            exerciseHint = "Dumbbell"
        ),
        Exercise(
            id = 1,
            name = "Push-ups",
            description = "A basic upper body strength exercise that works the chest, shoulders, and triceps.",
            durationMinutes = 5,
            exerciseSet = 3,
            category = "Strength",
            equipmentNeeded = listOf("None"),
            imageUrl = "https://example.com/images/pushups.png",
            videoUrl = null,
            exerciseReps = 10,
            exerciseHint = "Dumbbell"
        )

    )

    private val _workouts = MutableLiveData<Response<List<Workout>>>()

    val workouts: LiveData<Response<List<Workout>>> get() = _workouts

    fun getWorkouts() = applyResponse(_workouts, viewModelScope) { setWorks() }

    private fun setWorks() = listOf(
        Workout(
            id = 1,
            name = "Full Body Strength Training",
            description = "A comprehensive workout targeting all major muscle groups.",
            durationMinutes = 60,
            exercises = setExes(),
            targetedMuscleGroups = listOf("Chest", "Legs", "Back", "Shoulders", "Arms"),
            coachId = 123,
            difficultyLevel = "Intermediate",
            imageUrl = "https://example.com/images/full_body_workout.png",
            "Band"
        ),
        Workout(
            id = 1,
            name = "Full Body Strength Training",
            description = "A comprehensive workout targeting all major muscle groups.",
            durationMinutes = 60,
            exercises = setExes(),
            targetedMuscleGroups = listOf("Chest", "Legs", "Back", "Shoulders", "Arms"),
            coachId = 123,
            difficultyLevel = "Intermediate",
            imageUrl = "https://example.com/images/full_body_workout.png",
            "Band"
        )

    )

    private val _coaches = MutableLiveData<Response<List<Coach>>>()

    val coaches: LiveData<Response<List<Coach>>> get() = _coaches

    fun getCoaches() = applyResponse(_coaches, viewModelScope) { setCoaches() }

    private fun setCoaches(): List<Coach> = listOf(
        Coach(
            id = 1,
            name = "John Doe",
            specialization = "Strength Training",
            experienceYears = 10,
            certifications = listOf(
                "Certified Personal Trainer",
                "Strength and Conditioning Specialist"
            ),
            bio = "John has over 10 years of experience helping clients build strength and achieve their fitness goals.",
            profileImageUrl = "https://example.com/images/john_doe.png",
            contactEmail = "john.doe@example.com",
            contactPhone = "+1234567890",
            price = 250.00,
            rate = 4.4
        ),
        Coach(
            id = 1,
            name = "John Doe",
            specialization = "Strength Training",
            experienceYears = 10,
            certifications = listOf(
                "Certified Personal Trainer",
                "Strength and Conditioning Specialist"
            ),
            bio = "John has over 10 years of experience helping clients build strength and achieve their fitness goals.",
            profileImageUrl = "https://example.com/images/john_doe.png",
            contactEmail = "john.doe@example.com",
            contactPhone = "+1234567890",
            price = 250.00,
            rate = 4.4
        )

    )

}