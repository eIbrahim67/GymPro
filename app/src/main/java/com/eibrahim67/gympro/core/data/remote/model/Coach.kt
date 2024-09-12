package com.eibrahim67.gympro.core.data.remote.model

data class Coach(
    val id: Int,
    val name: String,
    val specializationIds: List<Int>, // e.g., "Strength Training", "Cardio", "Yoga"
    val experienceYears: Int, // Number of years of experience
    val certifications: List<String>?, // List of certifications the coach has
    val bio: String, // A brief biography of the coach
    val profileImageUrl: String? = null, // Optional: URL for the coach's profile image
    val contactEmail: String? = null, // Optional: Contact email for the coach
    val contactPhone: String? = null, // Optional: Contact phone number for the coach
    val price: Double,
    val rate: Double
)
