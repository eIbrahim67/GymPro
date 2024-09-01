package com.eibrahim67.gympro.home.model

data class Exercise(
    val id: Int,
    val name: String,
    val description: String,
    val durationMinutes: Int,
    val intensityLevel: Int, // e.g., 1 to 5 for intensity
    val category: TrainingCategory,
    val equipmentNeeded: List<String> = emptyList(),
    val imageUrl: String? = null ,
    val videoUrl: String? = null
)
