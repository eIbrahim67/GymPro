package com.eibrahim67.gympro.data.remote.model

data class TrainPlan(
    val id: Int,
    val name: String,
    val description: String,
    val durationDaysPerTrainingWeek: Int,
    val workoutsIds: List<Int>, // List of exercises included in the workout
    val targetedMuscleIds: List<Int>, // e.g., "Chest", "Legs", "Back"
    val coachId: String, // The coach who designed the workout
    val difficultyLevel: String, // e.g., "Beginner", "Intermediate", "Advanced"
    val imageUrl: String, // Optional: URL for an image representing the workout
    val trainingCategoriesIds: List<Int>,
    val avgTimeMinPerWorkout: Int

)
