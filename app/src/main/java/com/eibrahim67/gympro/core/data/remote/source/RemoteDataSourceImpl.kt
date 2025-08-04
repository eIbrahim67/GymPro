package com.eibrahim67.gympro.core.data.remote.source

import android.net.Uri
import com.eibrahim67.gympro.core.data.remote.model.Category
import com.eibrahim67.gympro.core.data.remote.model.Coach
import com.eibrahim67.gympro.core.data.remote.model.Exercise
import com.eibrahim67.gympro.core.data.remote.model.Muscles
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.model.Workout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class RemoteDataSourceImpl(
    private val db: FirebaseFirestore
) : RemoteDataSource {

    override suspend fun addMuscles(muscles: Map<Int, Muscles>) {
        val musclesMap = muscles.mapKeys { it.key.toString() }
            .mapValues { (_, muscle) ->
                mapOf(
                    "id" to muscle.id,
                    "name" to muscle.name
                )
            }

        db.collection("Data").document("muscles")
            .set(musclesMap)
            .await() // using kotlinx-coroutines-play-services
    }

    override suspend fun getMuscleById(id: Int): Muscles? {
        val documentSnapshot = db.collection("Data").document("muscles").get().await()
        val muscleMap = documentSnapshot.get(id.toString()) as? Map<*, *> ?: return null

        val muscleId = (muscleMap["id"] as? Long)?.toInt() ?: return null
        val muscleName = muscleMap["name"] as? String ?: return null

        return Muscles(muscleId, muscleName)
    }

    override suspend fun getAllMuscles(): Map<Int, Muscles> {
        val documentSnapshot = db.collection("Data").document("muscles").get().await()
        val result = mutableMapOf<Int, Muscles>()

        for ((key, value) in documentSnapshot.data ?: emptyMap()) {
            val intKey = key.toIntOrNull() ?: continue
            val muscleMap = value as? Map<*, *> ?: continue
            val id = (muscleMap["id"] as? Long)?.toInt() ?: continue
            val name = muscleMap["name"] as? String ?: continue
            result[intKey] = Muscles(id, name)
        }

        return result
    }

    override suspend fun addExercises(exercises: Map<Int, Exercise>) {
        val data = exercises.mapKeys { it.key.toString() }.mapValues { (_, ex) ->
            mapOf(
                "id" to ex.id,
                "name" to ex.name,
                "description" to ex.description,
                "exerciseHint" to ex.exerciseHint,
                "exerciseSet" to ex.exerciseSet,
                "exerciseReps" to ex.exerciseReps,
                "exerciseIntensity" to ex.exerciseIntensity,
                "categoryIds" to ex.categoryIds,
                "effectedMusclesIds" to ex.effectedMusclesIds,
                "imageUrl" to ex.imageUrl,
                "videoUrl" to ex.videoUrl
            )
        }

        db.collection("Data").document("exercises").set(data).await()
    }

    override suspend fun getExerciseById(id: Int): Exercise? {
        val documentSnapshot = db.collection("Data").document("exercises").get().await()
        val map = documentSnapshot.get(id.toString()) as? Map<*, *> ?: return null

        return Exercise(
            id = (map["id"] as? Long)?.toInt() ?: return null,
            name = map["name"] as? String ?: "",
            description = map["description"] as? String ?: "",
            exerciseHint = map["exerciseHint"] as? String ?: "",
            exerciseSet = (map["exerciseSet"] as? Long)?.toInt() ?: 0,
            exerciseReps = (map["exerciseReps"] as? Long)?.toInt() ?: 0,
            exerciseIntensity = (map["exerciseIntensity"] as? Long)?.toInt() ?: 0,
            categoryIds = (map["categoryIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() }
                ?: emptyList(),
            effectedMusclesIds = (map["effectedMusclesIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() }
                ?: emptyList(),
            imageUrl = map["imageUrl"] as? String ?: "",
            videoUrl = (map["videoUrl"] as? Uri ?: "") as Uri?
        )
    }

    override suspend fun getAllExercises(): Map<Int, Exercise> {
        val documentSnapshot = db.collection("Data").document("exercises").get().await()
        val result = mutableMapOf<Int, Exercise>()

        for ((key, value) in documentSnapshot.data ?: emptyMap()) {
            val intKey = key.toIntOrNull() ?: continue
            val map = value as? Map<*, *> ?: continue

            val exercise = Exercise(
                id = (map["id"] as? Long)?.toInt() ?: continue,
                name = map["name"] as? String ?: "",
                description = map["description"] as? String ?: "",
                exerciseHint = map["exerciseHint"] as? String ?: "",
                exerciseSet = (map["exerciseSet"] as? Long)?.toInt() ?: 0,
                exerciseReps = (map["exerciseReps"] as? Long)?.toInt() ?: 0,
                exerciseIntensity = (map["exerciseIntensity"] as? Long)?.toInt() ?: 0,
                categoryIds = (map["categoryIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() }
                    ?: emptyList(),
                effectedMusclesIds = (map["effectedMusclesIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() }
                    ?: emptyList(),
                imageUrl = map["imageUrl"] as? String ?: "",
                videoUrl = (map["videoUrl"] as? Uri ?: "") as Uri?
            )

            result[intKey] = exercise
        }

        return result
    }


    override suspend fun addWorkouts(workouts: Map<Int, Workout>) {
        val data = workouts.mapKeys { it.key.toString() }.mapValues { (_, w) ->
            mapOf(
                "id" to w.id,
                "name" to w.name,
                "description" to w.description,
                "durationMinutes" to w.durationMinutes,
                "exerciseIds" to w.exerciseIds,
                "targetedMuscleIds" to w.targetedMuscleIds,
                "coachId" to w.coachId,
                "difficultyLevel" to w.difficultyLevel,
                "imageUrl" to w.imageUrl,
                "equipment" to w.equipment
            )
        }

        db.collection("Data").document("workouts").set(data).await()
    }

    override suspend fun getWorkoutById(id: Int): Workout? {
        val snapshot = db.collection("Data").document("workouts").get().await()
        val map = snapshot.get(id.toString()) as? Map<*, *> ?: return null

        return Workout(
            id = (map["id"] as? Long)?.toInt() ?: return null,
            name = map["name"] as? String ?: "",
            description = map["description"] as? String ?: "",
            durationMinutes = (map["durationMinutes"] as? Long)?.toInt() ?: 0,
            exerciseIds = (map["exerciseIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() }
                ?: emptyList(),
            targetedMuscleIds = (map["targetedMuscleIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() }
                ?: emptyList(),
            coachId = map["coachId"] as? Int ?: -1,
            difficultyLevel = map["difficultyLevel"] as? String ?: "",
            imageUrl = map["imageUrl"] as? String ?: "",
            equipment = map["equipment"] as? String ?: ""
        )
    }

    override suspend fun getAllWorkouts(): Map<Int, Workout> {
        val snapshot = db.collection("Data").document("workouts").get().await()
        val result = mutableMapOf<Int, Workout>()

        for ((key, value) in snapshot.data ?: emptyMap()) {
            val intKey = key.toIntOrNull() ?: continue
            val map = value as? Map<*, *> ?: continue

            val workout = Workout(
                id = (map["id"] as? Long)?.toInt() ?: continue,
                name = map["name"] as? String ?: "",
                description = map["description"] as? String ?: "",
                durationMinutes = (map["durationMinutes"] as? Long)?.toInt() ?: 0,
                exerciseIds = (map["exerciseIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() }
                    ?: emptyList(),
                targetedMuscleIds = (map["targetedMuscleIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() }
                    ?: emptyList(),
                coachId = map["coachId"] as? Int ?: -1,
                difficultyLevel = map["difficultyLevel"] as? String ?: "",
                imageUrl = map["imageUrl"] as? String ?: "",
                equipment = map["equipment"] as? String ?: ""
            )

            result[intKey] = workout
        }

        return result
    }

    override suspend fun addTrainPlans(trainPlan: TrainPlan) {
        db.collection("Data").document("trainPlans")
            .set(hashMapOf(trainPlan.id.toString() to trainPlan), SetOptions.merge())
    }

    override suspend fun getMyTrainPlans(id: Int): List<String>? {
        val snapshot = db.collection("Data").document("coachPlansId").get().await()

        return snapshot.get(id.toString()) as? List<String> ?: return null
    }

    override suspend fun getTrainPlanById(id: Int): TrainPlan? {
        val snapshot = db.collection("Data").document("trainPlans").get().await()
        val map = snapshot.get(id.toString()) as? Map<*, *> ?: return null

        return TrainPlan(
            id = (map["id"] as? Long)?.toInt() ?: return null,
            name = map["name"] as? String ?: "",
            description = map["description"] as? String ?: "",
            durationDaysPerTrainingWeek = (map["durationDaysPerTrainingWeek"] as? Long)?.toInt()
                ?: 0,
            workoutsIds = (map["workoutsIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() }
                ?: emptyList(),
            targetedMuscleIds = (map["targetedMuscleIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() }
                ?: emptyList(),
            coachId = map["coachId"] as? Int ?: -1,
            difficultyLevel = map["difficultyLevel"] as? String ?: "",
            imageUrl = map["imageUrl"] as? String ?: "",
            trainingCategoriesIds = (map["trainingCategoriesIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() }
                ?: emptyList(),
            avgTimeMinPerWorkout = (map["avgTimeMinPerWorkout"] as? Long)?.toInt() ?: 0
        )
    }

    override suspend fun getAllTrainPlans(): Map<Int, TrainPlan> {
        val snapshot = db.collection("Data").document("trainPlans").get().await()
        val result = mutableMapOf<Int, TrainPlan>()

        for ((key, value) in snapshot.data ?: emptyMap()) {
            val intKey = key.toIntOrNull() ?: continue
            val map = value as? Map<*, *> ?: continue

            val plan = TrainPlan(
                id = (map["id"] as? Long)?.toInt() ?: continue,
                name = map["name"] as? String ?: "",
                description = map["description"] as? String ?: "",
                durationDaysPerTrainingWeek = (map["durationDaysPerTrainingWeek"] as? Long)?.toInt()
                    ?: 0,
                workoutsIds = (map["workoutsIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() }
                    ?: emptyList(),
                targetedMuscleIds = (map["targetedMuscleIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() }
                    ?: emptyList(),
                coachId = map["coachId"] as? Int ?: -1,
                difficultyLevel = map["difficultyLevel"] as? String ?: "",
                imageUrl = map["imageUrl"] as? String ?: "",
                trainingCategoriesIds = (map["trainingCategoriesIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() }
                    ?: emptyList(),
                avgTimeMinPerWorkout = (map["avgTimeMinPerWorkout"] as? Long)?.toInt() ?: 0
            )

            result[intKey] = plan
        }

        return result
    }

    override suspend fun addCategories(categories: Map<Int, Category>) {
        val data = categories.mapKeys { it.key.toString() }.mapValues { (_, c) ->
            mapOf(
                "id" to c.id,
                "name" to c.name,
                "description" to c.description,
                "iconUrl" to c.iconUrl
            )
        }

        db.collection("Data").document("categories").set(data).await()
    }

    override suspend fun getCategoryById(id: Int): Category? {
        val snapshot = db.collection("Data").document("categories").get().await()
        val map = snapshot.get(id.toString()) as? Map<*, *> ?: return null

        return Category(
            id = (map["id"] as? Long)?.toInt() ?: return null,
            name = map["name"] as? String ?: "",
            description = map["description"] as? String ?: "",
            iconUrl = map["iconUrl"] as? String ?: ""
        )
    }

    override suspend fun getAllCategories(): Map<Int, Category> {
        val snapshot = db.collection("Data").document("categories").get().await()
        val result = mutableMapOf<Int, Category>()

        for ((key, value) in snapshot.data ?: emptyMap()) {
            val intKey = key.toIntOrNull() ?: continue
            val map = value as? Map<*, *> ?: continue

            val category = Category(
                id = (map["id"] as? Long)?.toInt() ?: continue,
                name = map["name"] as? String ?: "",
                description = map["description"] as? String ?: "",
                iconUrl = map["iconUrl"] as? String ?: ""
            )

            result[intKey] = category
        }

        return result
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

}