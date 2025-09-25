package com.eibrahim67.gympro.data.remote.model

data class Exercise(
    val id: Int,
    val name: String,
    val description: String,
    val exerciseHint: String,
    val exerciseSet: Int,
    val exerciseReps: Int,
    val coachId: String,
    val exerciseIntensity: Int,
    val categoryIds: List<Int>,
    val effectedMusclesIds: List<Int>,
    val imageUrl: String,
    val videoUrl: String?
)
