package com.eibrahim67.gympro.core.data.remote.model

data class Workout(
    val id: Int,
    val name: String,
    val description: String,
    val durationMinutes: Int,
    val exerciseIds: List<Int>, // List of exercises included in the workout
    val targetedMuscleIds: List<Int>, // e.g., "Chest", "Legs", "Back"
    val coachId: Int, // The coach who designed the workout
    val difficultyLevel: String, // e.g., "Beginner", "0", "Advanced"
    val imageUrl: String, // Optional: URL for an image representing the workout
    val equipment: String?,
    var targetedMusclesAsString: String? = null
)
