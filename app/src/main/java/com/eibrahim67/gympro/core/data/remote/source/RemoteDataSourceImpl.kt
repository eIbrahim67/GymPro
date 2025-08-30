package com.eibrahim67.gympro.core.data.remote.source

import android.util.Log
import com.eibrahim67.gympro.core.data.remote.model.Category
import com.eibrahim67.gympro.core.data.remote.model.Coach
import com.eibrahim67.gympro.core.data.remote.model.Exercise
import com.eibrahim67.gympro.core.data.remote.model.Muscles
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.model.Workout
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class RemoteDataSourceImpl(
    private val db: FirebaseFirestore
) : RemoteDataSource {

    override suspend fun addMuscles(muscles: Map<Int, Muscles>) {
        val musclesMap = muscles.mapKeys { it.key.toString() }.mapValues { (_, muscle) ->
            mapOf(
                "id" to muscle.id, "name" to muscle.name
            )
        }

        db.collection("Data").document("muscles").set(musclesMap).await()
    }

    override suspend fun getMuscleById(id: Int): Muscles? {
        val documentSnapshot = db.collection("Data").document("muscles").get().await()
        val muscleMap = documentSnapshot.get(id.toString()) as? Map<*, *> ?: return null

        val muscleId = (muscleMap["id"] as? Long)?.toInt() ?: return null
        val muscleName = muscleMap["name"] as? String ?: return null

        return Muscles(muscleId, muscleName)
    }

    override suspend fun getMusclesByIds(ids: List<Int>): List<Muscles>? {
        val documentSnapshot = db.collection("Data").document("muscles").get().await()
        val list = mutableListOf<Muscles>()
        for (id in ids) {
            val muscleMap = documentSnapshot.get(id.toString()) as? Map<*, *> ?: return null

            val muscleId = (muscleMap["id"] as? Long)?.toInt() ?: return null
            val muscleName = muscleMap["name"] as? String ?: return null
            list.add(Muscles(muscleId, muscleName))
        }

        return list
    }

    override suspend fun getAllMuscles(): List<Muscles> {
        val documentSnapshot = db.collection("Data").document("muscles").get().await()
        val result = mutableListOf<Muscles>()

        for ((_, data) in documentSnapshot.data ?: emptyMap()) {
            val muscleMap = data as? Map<*, *> ?: continue
            val id = (muscleMap["id"] as? Long)?.toInt() ?: continue
            val name = muscleMap["name"] as? String ?: continue
            result.add(Muscles(id, name))
        }

        return result
    }


    override suspend fun addExercises(exercises: Exercise) {
        db.collection("Data").document("exercises")
            .set(hashMapOf(exercises.id.toString() to exercises), SetOptions.merge())
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
            videoUrl = map["videoUrl"] as? String ?: "",
            coachId = (map["coachId"] as? Long)?.toInt() ?: -1)
    }

    override suspend fun getExercisesByIds(ids: List<Int>): List<Exercise>? {
        val documentSnapshot = db.collection("Data").document("exercises").get().await()
        val list = mutableListOf<Exercise>()
        for (id in ids) {
            val map = documentSnapshot.get(id.toString()) as? Map<*, *> ?: return null

            val exercise = Exercise(
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
                videoUrl = map["videoUrl"] as? String ?: "",
                coachId = (map["coachId"] as? Long)?.toInt() ?: -1)

            list.add(exercise)
        }
        return list
    }

    override suspend fun getAllExercises(): List<Exercise?> {
        val documentSnapshot = db.collection("Data").document("exercises").get().await()
        val result = mutableListOf<Exercise>()

        for ((_, value) in documentSnapshot.data ?: emptyMap()) {
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
                videoUrl = map["videoUrl"] as? String ?: "",
                coachId = (map["coachId"] as? Long)?.toInt() ?: -1)

            result.add(exercise)
        }

        return result
    }

    override suspend fun getMyExercisesIds(id: Int): List<Int>? {
        val snapshot = db.collection("Data").document("coachExercisesId").get().await()

        return snapshot.get(id.toString()) as? List<Int>
    }

    override suspend fun addExerciseId(coachId: Int, newWorkoutId: Int) {
        val docRef = db.collection("Data").document("coachExercisesId")

        docRef.update(
            coachId.toString(), FieldValue.arrayUnion(newWorkoutId)
        ).addOnSuccessListener {
            Log.d("Firestore", "Exercise ID added successfully")
        }.addOnFailureListener { e ->
            Log.e("Firestore", "Error updating list", e)
        }
    }

    override suspend fun deleteExercise(id: Int) {
        db.collection("exercises").document(id.toString())
            .update(mapOf(id.toString() to FieldValue.delete())).addOnSuccessListener {}
            .addOnFailureListener {}
    }


    override suspend fun addWorkouts(workout: Workout) {
        db.collection("Data").document("workouts")
            .set(hashMapOf(workout.id.toString() to workout), SetOptions.merge())
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
            equipment = map["equipment"] as? String ?: "")
    }

    override suspend fun getWorkoutsByIds(ids: List<Int>): List<Workout>? {
        val list = mutableListOf<Workout>()
        val snapshot = db.collection("Data").document("workouts").get().await()

        try {
            for (id in ids) {
                val map = snapshot.get(id.toString()) as? Map<*, *> ?: continue
                val workout = Workout(
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
                    equipment = map["equipment"] as? String ?: "")
                list.add(workout)
            }
        } catch (e: Exception) {
        }

        return list
    }

    override suspend fun getAllWorkouts(): List<Workout> {
        val snapshot = db.collection("Data").document("workouts").get().await()
        val result = mutableListOf<Workout>()

        for ((_, data) in snapshot.data ?: emptyMap()) {
            val map = data as? Map<*, *> ?: continue

            val workout = Workout(
                id = (map["id"] as? Long)?.toInt() ?: continue,
                name = map["name"] as? String ?: "",
                description = map["description"] as? String ?: "",
                durationMinutes = (map["durationMinutes"] as? Long)?.toInt() ?: 0,
                exerciseIds = (map["exerciseIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() }
                    ?: emptyList(),
                targetedMuscleIds = (map["targetedMuscleIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() }
                    ?: emptyList(),
                coachId = (map["coachId"] as? Long)?.toInt() ?: -1,
                difficultyLevel = map["difficultyLevel"] as? String ?: "",
                imageUrl = map["imageUrl"] as? String ?: "",
                equipment = map["equipment"] as? String ?: "")

            result.add(workout)
        }

        return result
    }

    override suspend fun getMyWorkoutsIds(id: Int): List<Int>? {
        val snapshot = db.collection("Data").document("coachWorkoutsId").get().await()

        return snapshot.get(id.toString()) as? List<Int>
    }

    override suspend fun addWorkoutId(coachId: Int, newWorkoutId: Int) {
        val docRef = db.collection("Data").document("coachWorkoutsId")

        docRef.update(
            coachId.toString(), FieldValue.arrayUnion(newWorkoutId)
        ).addOnSuccessListener {
            Log.d("Firestore", "Workout ID added successfully")
        }.addOnFailureListener { e ->
            Log.e("Firestore", "Error updating list", e)
        }
    }

    override suspend fun deleteWorkout(id: Int) {
        db.collection("Data").document("workouts")
            .update(mapOf(id.toString() to FieldValue.delete())).addOnSuccessListener {}
            .addOnFailureListener {}
    }

    override suspend fun addTrainPlans(trainPlan: TrainPlan) {
        db.collection("Data").document("trainPlans")
            .set(hashMapOf(trainPlan.id.toString() to trainPlan), SetOptions.merge())
    }

    override suspend fun addTrainPlanId(coachId: Int, newPlanId: Int) {

        val snapshot = db.collection("Data").document("coachPlansId").get().await()
        val docRef = db.collection("Data").document("coachPlansId")

        val list = snapshot.get(coachId.toString()) as? MutableList<Int> ?: mutableListOf()
        list.add(newPlanId)
        val mapToUpdate = hashMapOf(
            coachId.toString() to list
        )

        docRef.set(mapToUpdate, SetOptions.merge()).addOnSuccessListener {
            Log.d("Firestore", "List added to coach ID successfully")
        }.addOnFailureListener { e ->
            Log.e("Firestore", "Error updating list", e)
        }

    }

    override suspend fun getMyTrainPlansIds(id: Int): List<Int>? {
        val snapshot = db.collection("Data").document("coachPlansId").get().await()

        return snapshot.get(id.toString()) as? List<Int>
    }

    override suspend fun deleteTrainPlan(id: Int) {
        db.collection("trainPlans").document(id.toString())
            .update(mapOf(id.toString() to FieldValue.delete())).addOnSuccessListener {}
            .addOnFailureListener {}
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
            avgTimeMinPerWorkout = (map["avgTimeMinPerWorkout"] as? Long)?.toInt() ?: 0)
    }

    override suspend fun getTrainPlanByIds(ids: List<Int>): List<TrainPlan> {
        val list = mutableListOf<TrainPlan>()

        try {
            for (id in ids) {
                val snapshot = db.collection("Data").document("trainPlans").get().await()
                val map = snapshot.get(id.toString()) as? Map<*, *> ?: continue
                val trainPlan = TrainPlan(
                    id = (map["id"] as? Long)?.toInt() ?: -1,
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
                    avgTimeMinPerWorkout = (map["avgTimeMinPerWorkout"] as? Long)?.toInt() ?: 0)
                list.add(trainPlan)
            }
        } catch (e: Exception) {
        }

        return list
    }

    override suspend fun getAllTrainPlans(): List<TrainPlan> {
        val snapshot = db.collection("Data").document("trainPlans").get().await()
        val result = mutableListOf<TrainPlan>()
        try {
            for ((_, value) in snapshot.data ?: emptyMap()) {

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
                    avgTimeMinPerWorkout = (map["avgTimeMinPerWorkout"] as? Long)?.toInt() ?: 0)
                result.add(plan)
            }
        } catch (e: Exception) {
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

    override suspend fun getAllCategories(): List<Category> {
        val snapshot = db.collection("Data").document("categories").get().await()
        val result = mutableListOf<Category>()

        for ((_, value) in snapshot.data ?: emptyMap()) {
            val map = value as? Map<*, *> ?: continue

            val category = Category(
                id = (map["id"] as? Long)?.toInt() ?: continue,
                name = map["name"] as? String ?: "",
                description = map["description"] as? String ?: "",
                iconUrl = map["iconUrl"] as? String ?: ""
            )

            result.add(category)
        }

        return result
    }

    override suspend fun addCoach(coach: Coach) {
        db.collection("Data").document("coaches")
            .set(hashMapOf(coach.id.toString() to coach), SetOptions.merge())
    }

    override suspend fun getCoachById(id: Int): Coach? {
        val snapshot = db.collection("Data").document("coaches").get().await()
        val map = snapshot.get(id.toString()) as? Map<*, *> ?: return null

        return Coach(
            id = (map["id"] as? Long)?.toInt() ?: return null,
            name = map["name"] as? String ?: "",
            specializationIds = (map["specializationIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() }
                ?: emptyList(),
            experienceYears = (map["experienceYears"] as? Long)?.toInt() ?: 0,
            certifications = (map["certifications"] as? List<*>)?.mapNotNull { it as? String },
            bio = map["bio"] as? String ?: "",
            profileImageUrl = map["profileImageUrl"] as? String,
            contactEmail = map["contactEmail"] as? String,
            contactPhone = map["contactPhone"] as? String,
            price = (map["price"] as? Double) ?: ((map["price"] as? Long)?.toDouble() ?: 0.0),
            rate = (map["rate"] as? Double) ?: ((map["rate"] as? Long)?.toDouble() ?: 0.0)
        )

    }

    override suspend fun getAllCoaches(): List<Coach> {
        val snapshot = db.collection("Data").document("coaches").get().await()
        val result = mutableListOf<Coach>()
        for ((_, value) in snapshot.data ?: emptyMap()) {
            val map = value as? Map<*, *> ?: continue
            val coach = Coach(
                id = (map["id"] as? Long)?.toInt() ?: continue,
                name = map["name"] as? String ?: "",
                specializationIds = (map["specializationIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() }
                    ?: emptyList(),
                experienceYears = (map["experienceYears"] as? Long)?.toInt() ?: 0,
                certifications = (map["certifications"] as? List<*>)?.mapNotNull { it as? String },
                bio = map["bio"] as? String ?: "",
                profileImageUrl = map["profileImageUrl"] as? String,
                contactEmail = map["contactEmail"] as? String,
                contactPhone = map["contactPhone"] as? String,
                price = (map["price"] as? Double) ?: ((map["price"] as? Long)?.toDouble() ?: 0.0),
                rate = (map["rate"] as? Double) ?: ((map["rate"] as? Long)?.toDouble() ?: 0.0)
            )

            result.add(coach)
        }

        return result
    }
}