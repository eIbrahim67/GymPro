package com.eibrahim67.gympro.home.model

data class Workout(
    val id: Int,
    val name: String,
    val description: String,
    val durationMinutes: Int,
    val exercisesIds: List<Int>, // List of exercises included in the workout
    val targetedMuscleGroups: List<String>, // e.g., "Chest", "Legs", "Back"
    val coachId: Int, // The coach who designed the workout
    val difficultyLevel: String, // e.g., "Beginner", "Intermediate", "Advanced"
    val imageUrl: String? = null, // Optional: URL for an image representing the workout
    val equipment : String? = null
)
