package com.eibrahim67.gympro.core.data.remote.source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eibrahim67.gympro.core.data.remote.model.Category
import com.eibrahim67.gympro.core.data.remote.model.Coach
import com.eibrahim67.gympro.core.data.remote.model.Exercise
import com.eibrahim67.gympro.core.data.remote.model.Muscles
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.model.Workout
import com.google.firebase.firestore.FirebaseFirestore

class RemoteDataSourceImpl(
    private val db: FirebaseFirestore
) : RemoteDataSource {

    override suspend fun addCategory(categories: Map<Int, Category>) {

        db.collection("Data").document("categories")
            .set(
                categories.mapKeys { entry -> entry.key.toString() }.mapValues { entry ->
                    mapOf(
                        "id" to entry.value.id,
                        "name" to entry.value.name,
                        "description" to entry.value.description,
                        "iconUrl" to entry.value.iconUrl
                    )
                }
            )
    }

    override suspend fun addMuscle(muscles: Map<Int, Muscles>) {
        db.collection("Data").document("muscles")
            .set(
                muscles.mapKeys { entry -> entry.key.toString() }.mapValues { entry ->
                    mapOf(
                        "id" to entry.value.id,
                        "name" to entry.value.name
                    )
                }
            )
    }

    override suspend fun addCoach(coach: Map<Int, Coach>) {
        db.collection("Data").document("coaches")
            .set(
                coach.mapKeys { entry -> entry.key.toString() }.mapValues { entry ->
                    mapOf(
                        "id" to entry.value.id,
                        "name" to entry.value.name,
                        "specializationIds" to entry.value.specializationIds,
                        "experienceYears" to entry.value.experienceYears,
                        "certifications" to entry.value.certifications,
                        "bio" to entry.value.bio,
                        "profileImageUrl" to entry.value.profileImageUrl,
                        "contactEmail" to entry.value.contactEmail,
                        "contactPhone" to entry.value.contactPhone,
                        "price" to entry.value.price,
                        "rate" to entry.value.rate
                    )
                }
            )
    }

    override suspend fun addExercise(exercise: Map<Int, Exercise>) {
        db.collection("Data").document("exercises")
            .set(
                exercise.mapKeys { entry -> entry.key.toString() }.mapValues { entry ->
                    mapOf(
                        "id" to entry.value.id,
                        "name" to entry.value.name,
                        "description" to entry.value.description,
                        "exerciseHint" to entry.value.exerciseHint,
                        "exerciseSet" to entry.value.exerciseSet,
                        "exerciseReps" to entry.value.exerciseReps,
                        "exerciseIntensity" to entry.value.exerciseIntensity,
                        "categoryIds" to entry.value.categoryIds,
                        "effectedMusclesIds" to entry.value.effectedMusclesIds,
                        "imageUrl" to entry.value.imageUrl,
                        "videoUrl" to entry.value.videoUrl
                    )
                }
            )
    }

    override suspend fun addWorkout(workout: Map<Int, Workout>) {
        db.collection("Data").document("workouts")
            .set(
                workout.mapKeys { entry -> entry.key.toString() }.mapValues { entry ->
                    mapOf(
                        "id" to entry.value.id,
                        "name" to entry.value.name,
                        "description" to entry.value.description,
                        "durationMinutes" to entry.value.durationMinutes,
                        "exerciseIds" to entry.value.exerciseIds,
                        "targetedMuscleIds" to entry.value.targetedMuscleIds,
                        "coachId" to entry.value.coachId,
                        "difficultyLevel" to entry.value.difficultyLevel,
                        "imageUrl" to entry.value.imageUrl,
                        "equipment" to entry.value.equipment
                    )
                }
            )
    }

    override suspend fun addTrainPlan(trainPlan: Map<Int, TrainPlan>) {
        db.collection("Data").document("trainPlans")
            .set(
                trainPlan.mapKeys { entry -> entry.key.toString() }.mapValues { entry ->
                    mapOf(
                        "id" to entry.value.id,
                        "name" to entry.value.name,
                        "description" to entry.value.description,
                        "durationDaysPerTrainingWeek" to entry.value.durationDaysPerTrainingWeek,
                        "workoutsIds" to entry.value.workoutsIds,
                        "targetedMuscleIds" to entry.value.targetedMuscleIds,
                        "coachId" to entry.value.coachId,
                        "difficultyLevel" to entry.value.difficultyLevel,
                        "imageUrl" to entry.value.imageUrl,
                        "trainingCategoriesIds" to entry.value.trainingCategoriesIds,
                        "avgTimeMinPerWorkout" to entry.value.avgTimeMinPerWorkout
                    )
                }
            )
    }

    override suspend fun getCategories(): LiveData<List<Category>> {
        val categoriesLiveData = MutableLiveData<List<Category>>()
        db.collection("Data").document("categories")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Convert Firestore data to Map<String, Category>
                    val categoriesMap = document.data
                    val categoriesList = categoriesMap?.map { entry ->
                        val categoryData = entry.value as Map<*, *>
                        Category(
                            id = (categoryData["id"] as Long).toInt(),
                            name = categoryData["name"] as String,
                            description = categoryData["description"] as String,
                            iconUrl = categoryData["iconUrl"] as String
                        )
                    } ?: emptyList()

                    categoriesLiveData.value = categoriesList
                    Log.d("Firestore", categoriesList.toString())
                } else {
                    Log.d("Firestore", "No categories found")
                    categoriesLiveData.value = emptyList()
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting categories", exception)
                categoriesLiveData.value = emptyList()
            }

        return categoriesLiveData
    }


}