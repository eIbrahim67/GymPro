package com.eibrahim67.gympro.core.data.writtenData.source

import com.eibrahim67.gympro.core.data.writtenData.model.Category
import com.eibrahim67.gympro.core.data.writtenData.model.Coach
import com.eibrahim67.gympro.core.data.writtenData.model.Exercise
import com.eibrahim67.gympro.core.data.writtenData.model.Muscles
import com.eibrahim67.gympro.core.data.writtenData.model.TrainPlan
import com.eibrahim67.gympro.core.data.writtenData.model.Workout

object SourceWrittenData {

    private val categories: Map<Int, Category> = mapOf(

        Pair(
            1, Category(
                id = 1,
                name = "Strength",
                description = "",
                iconUrl = ""
            )

        )
    )

    private val muscles: Map<Int, Muscles> =
        mapOf(
            Pair(0, Muscles(0, "All body")),
            Pair(1, Muscles(1, "Chest")),
            Pair(2, Muscles(2, "Front Shoulder")),
            Pair(3, Muscles(3, "Triceps"))
        )

    private val coaches: Map<Int, Coach> = mapOf(

        Pair(
            1, Coach(
                id = 1,
                name = "Ibrahim Mohamed",
                specialization = "Strength Training",
                experienceYears = 1,
                certifications = null,
                bio = "I have over a year of experience in training and researching muscle growth techniques to effectively increase muscle mass and overall fitness.",
                profileImageUrl = "",
                contactEmail = "ibrahim.mohamed.coach@gmail.com",
                contactPhone = "01550162282",
                price = 250.00,
                rate = 4.6
            )
        )
    )

    private val exercises: Map<Int, Exercise> = mapOf(

        Pair(
            1, Exercise(
                id = 1,
                name = "Incline Bench Press",
                description = "",
                exerciseHint = "Dumbbell",
                exerciseSet = 3,
                exerciseReps = 8,
                exerciseIntensity = 90,
                categoryIds = listOf(1),
                effectedMusclesIds = listOf(1),
                imageUrl = "",
                videoUrl = ""
            )
        ),
        Pair(
            2, Exercise(
                id = 2,
                name = "Cable Flat Bench",
                description = "",
                exerciseHint = "Cable",
                exerciseSet = 3,
                exerciseReps = 8,
                exerciseIntensity = 90,
                categoryIds = listOf(1),
                effectedMusclesIds = listOf(1),
                imageUrl = "",
                videoUrl = ""
            )
        ),
        Pair(
            3, Exercise(
                id = 3,
                name = "Push Up",
                description = "",
                exerciseHint = "Object to be able to get the long stretch",
                exerciseSet = 2,
                exerciseReps = 8,
                exerciseIntensity = 90,
                categoryIds = listOf(1),
                effectedMusclesIds = listOf(1),
                imageUrl = "",
                videoUrl = ""
            )
        )
    )

    private val workouts: Map<Int, Workout> = mapOf(

        Pair(
            1, Workout(
                id = 1,
                "Chest and Triceps",
                description = "",
                durationMinutes = 60,
                exerciseIds = listOf(1, 2, 3),
                targetedMuscleIds = listOf(1, 2, 3),
                coachId = 1,
                difficultyLevel = "Intermediate",
                imageUrl = "",
                equipment = null
            )
        )

    )

    private val trainPlans: Map<Int, TrainPlan> = mapOf(

        Pair(
            1, TrainPlan(
                id = 1,
                name = "Low Volume High Intensity V1",
                description = "",
                durationDaysPerTrainingWeek = 5,
                workoutsIds = listOf(1),
                targetedMuscleIds = listOf(0),
                coachId = 1,
                difficultyLevel = "Intermediate",
                imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzr3YQCFB8-y9_fpIEkFOLLyie0GOs_J_IBAkkrzUpeX4PiDZANiPLJE4&s=10",
                trainingCategoriesIds = listOf(1),
                avgTimeMinPerWorkout = 100
            )
        )

    )

    fun getCategoriesData() = categories.values.toList()

    fun getMusclesData() = muscles.values.toList()

    fun getCoachesData() = coaches.values.toList()

    fun getExercisesData() = exercises.values.toList()

    fun getWorkoutsData() = workouts.values.toList()

    fun getTrainingPlansData() = trainPlans.values.toList()

    fun getTrainingPlansById(id: Int) = trainPlans[id]

    fun getCoachById(id: Int) = coaches[id]

    fun targetedMuscleById(id: Int) = muscles[id]

    fun getTargetedMusclesByIdsAsString(ids: List<Int>): String {

        val targetedMuscle: MutableList<String> = mutableListOf()

        ids.forEach { targetedMuscle.add(targetedMuscleById(it)?.name.toString()) }

        return targetedMuscle.joinToString(separator = " ,")
    }

    fun getWorkoutById(id: Int) = workouts[id]

    fun getWorkoutsByIds(ids: List<Int>): List<Workout> {

        val workoutList: MutableList<Workout> = mutableListOf()

        ids.forEach { id ->
            getWorkoutById(id)?.let { workout ->
                workout.targetedMusclesAsString =
                    getTargetedMusclesByIdsAsString(workout.targetedMuscleIds)
                workoutList.add(workout)
            }
        }

        return workoutList

    }

    fun getExerciseById(id: Int) = exercises[id]

    fun getExercisesByIds(ids: List<Int>): List<Exercise> {

        val exercisesList: MutableList<Exercise> = mutableListOf()

        ids.forEach { id ->
            getExerciseById(id)?.let { exercise ->
                exercisesList.add(exercise)
            }
        }

        return exercisesList

    }

}