package com.eibrahim67.gympro.train.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse
import com.eibrahim67.gympro.home.model.Exercise
import com.eibrahim67.gympro.home.model.TrainPlan
import com.eibrahim67.gympro.home.model.Workout

class TrainViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _trainPlanId = MutableLiveData<Response<Int?>>()

    private val trainPlanId: LiveData<Response<Int?>> get() = _trainPlanId

    private fun getTrainPlanId() = applyResponse(_trainPlanId, viewModelScope) {
        userRepository.getTrainPlanId()
    }

    private val _userHaveTrainer = MutableLiveData<Response<Boolean>>()

    val userHaveTrainer: LiveData<Response<Boolean>> get() = _userHaveTrainer

    fun isUserHaveTrainer() = applyResponse(
        _userHaveTrainer,
        viewModelScope
    ) { userRepository.isLoggedInUserHaveTrainer() }

    private val _trainPlan = MutableLiveData<Response<TrainPlan>>()

    val trainPlan: LiveData<Response<TrainPlan>> get() = _trainPlan

    fun getTrainPlan() {
        applyResponse(_trainPlan, viewModelScope) { setTrainPlan() }
    }

    private fun setTrainPlan() = TrainPlan(
        id = 1,
        name = "Full Body Strength Training",
        description = "A comprehensive workout targeting all major muscle groups.",
        durationDaysPerWeek = "5 days per week",
        workoutsList = setWorks(),
        targetedMuscleGroups = listOf("Chest", "Legs", "Back", "Shoulders", "Arms"),
        coachId = 123,
        difficultyLevel = "Intermediate",
        imageUrl = "https://example.com/images/full_body_workout.png"
    )

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

}