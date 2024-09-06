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
            Pair(2, Muscles(2, "Upper Chest")),
            Pair(3, Muscles(3, "Lower Chest")),
            Pair(4, Muscles(4, "Front Shoulder")),
            Pair(5, Muscles(5, "Side Shoulder")),
            Pair(6, Muscles(6, "back Shoulder")),
            Pair(7, Muscles(7, "Biceps")),
            Pair(8, Muscles(8, "Long Biceps")),
            Pair(9, Muscles(9, "Short Biceps")),
            Pair(10, Muscles(10, "Triceps")),
            Pair(11, Muscles(11, "Long Triceps")),
            Pair(12, Muscles(12, "Short Triceps")),
            Pair(13, Muscles(13, "Lateral Triceps")),
            Pair(14, Muscles(14, "Rest1")),
            Pair(15, Muscles(15, "Rest2")),
            Pair(16, Muscles(16, "Rest3")),
            Pair(17, Muscles(17, "Back")),
            Pair(18, Muscles(18, "Upper Back")),
            Pair(19, Muscles(19, "Lower Back")),
            Pair(20, Muscles(20, "Lats")),
            Pair(21, Muscles(21, "Terbaz")),
            Pair(22, Muscles(22, "Leg1")),
            Pair(23, Muscles(23, "Leg2")),
            Pair(24, Muscles(24, "Claves")),
            Pair(25, Muscles(25, "Abs")),
            Pair(26, Muscles(26, "Abs2")),
            Pair(27, Muscles(27, "Gluts")),
        )

    private val coaches: Map<Int, Coach> = mapOf(

        Pair(
            1, Coach(
                id = 1,
                name = "Ibrahim Mohamed",
                specializationIds = listOf(1),
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
                effectedMusclesIds = listOf(2, 4),
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
                effectedMusclesIds = listOf(3),
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
        ),
        Pair(
            4, Exercise(
                id = 4,
                name = "Lat Pull Down",
                description = "",
                exerciseHint = "Machine",
                exerciseSet = 3,
                exerciseReps = 8,
                exerciseIntensity = 90,
                categoryIds = listOf(1),
                effectedMusclesIds = listOf(18, 20),
                imageUrl = "",
                videoUrl = ""
            )
        ),
        Pair(
            5, Exercise(
                id = 5,
                name = "Back Pull Narrow",
                description = "",
                exerciseHint = "Machine",
                exerciseSet = 3,
                exerciseReps = 8,
                exerciseIntensity = 90,
                categoryIds = listOf(1),
                effectedMusclesIds = listOf(18, 20),
                imageUrl = "",
                videoUrl = ""
            )
        ),
        Pair(
            6, Exercise(
                id = 6,
                name = "Back High Pull One Arm",
                description = "",
                exerciseHint = "Cable",
                exerciseSet = 3,
                exerciseReps = 8,
                exerciseIntensity = 90,
                categoryIds = listOf(1),
                effectedMusclesIds = listOf(18, 20),
                imageUrl = "",
                videoUrl = ""
            )
        ),
        Pair(
            7, Exercise(
                id = 7,
                name = "Hummer Biceps",
                description = "",
                exerciseHint = "Dumbbell",
                exerciseSet = 3,
                exerciseReps = 8,
                exerciseIntensity = 90,
                categoryIds = listOf(1),
                effectedMusclesIds = listOf(14, 7),
                imageUrl = "",
                videoUrl = ""
            )
        ),
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
        ),
        Pair(
            2, Workout(
                id = 2,
                "Back and Biceps",
                description = "",
                durationMinutes = 90,
                exerciseIds = listOf(),
                targetedMuscleIds = listOf(4, 5, 6, 7),
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
                workoutsIds = listOf(1, 2),
                targetedMuscleIds = listOf(0),
                coachId = 1,
                difficultyLevel = "Intermediate",
                imageUrl = "",
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

    private fun targetedMuscleById(id: Int) = muscles[id]

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