package com.eibrahim67.gympro.home.model

data class TrainPlan(
    val id: Int,
    val name: String,
    val description: String,
    val durationDaysPerWeek: String,
    val workoutsList: List<Workout>, // List of exercises included in the workout
    val targetedMuscleGroups: List<String>, // e.g., "Chest", "Legs", "Back"
    val coachId: Int, // The coach who designed the workout
    val difficultyLevel: String, // e.g., "Beginner", "Intermediate", "Advanced"
    val imageUrl: String? = null, // Optional: URL for an image representing the workout

)
