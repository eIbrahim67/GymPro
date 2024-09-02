package com.eibrahim67.gympro.home.model

data class Exercise(
    val id: Int,
    val name: String,
    val description: String,
    val exerciseSet: Int,
    val exerciseReps: Int,
    val categoryIds: Int,
    val equipmentNeeded: List<String> = emptyList(),
    val imageUrl: String? = null,
    val videoUrl: String? = null,
    val exerciseHint: String? = null
)
