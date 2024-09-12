package com.eibrahim67.gympro.core.data.writtenData.source

import com.eibrahim67.gympro.core.data.remote.model.Category
import com.eibrahim67.gympro.core.data.remote.model.Coach
import com.eibrahim67.gympro.core.data.remote.model.Exercise
import com.eibrahim67.gympro.core.data.remote.model.Muscles
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.model.Workout

object SourceWrittenData {

     private val categories: Map<Int, Category> = mapOf(
        1 to Category(
            id = 1,
            name = "Strength",
            description = "Exercises focused on improving muscle strength, such as weightlifting and resistance training.",
            iconUrl = "https://images.pexels.com/photos/1431282/pexels-photo-1431282.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
        ),
        2 to Category(
            id = 2,
            name = "Cardio",
            description = "Exercises aimed at increasing heart rate and improving cardiovascular health, such as running, cycling, and HIIT.",
            iconUrl = "https://images.pexels.com/photos/3253501/pexels-photo-3253501.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
        ),
        3 to Category(
            id = 3,
            name = "Flexibility",
            description = "Exercises designed to improve range of motion and flexibility, including yoga and stretching routines.",
            iconUrl = "https://images.pexels.com/photos/6572614/pexels-photo-6572614.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
        ),
        4 to Category(
            id = 4,
            name = "Endurance",
            description = "Training focused on increasing stamina and endurance, typically involving prolonged physical activity like long-distance running or swimming.",
            iconUrl = "https://images.pexels.com/photos/1465893/pexels-photo-1465893.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
        ),
        5 to Category(
            id = 5,
            name = "Balance",
            description = "Exercises aimed at improving stability and coordination, such as balance ball exercises and stability training.",
            iconUrl = "https://images.pexels.com/photos/6572650/pexels-photo-6572650.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
        )
    )

    private val muscles: Map<Int, Muscles> = mapOf(
        0 to Muscles(0, "All body"),
        1 to Muscles(1, "Chest"),
        2 to Muscles(2, "Upper Chest"),
        3 to Muscles(3, "Lower Chest"),
        4 to Muscles(4, "Anterior Deltoid"),
        5 to Muscles(5, "Lateral Deltoid"),
        6 to Muscles(6, "Posterior Deltoid"),
        7 to Muscles(7, "Biceps"),
        8 to Muscles(8, "Long Biceps"),
        9 to Muscles(9, "Short Biceps"),
        10 to Muscles(10, "Triceps"),
        11 to Muscles(11, "Long Triceps"),
        12 to Muscles(12, "Short Triceps"),
        13 to Muscles(13, "Lateral Triceps"),
        14 to Muscles(14, "Brachioradialis"),
        15 to Muscles(15, "Flexors"),
        16 to Muscles(16, "Extensor"),
        17 to Muscles(17, "Back"),
        18 to Muscles(18, "Upper Back"),
        19 to Muscles(19, "Lower Back"),
        20 to Muscles(20, "Lats"),
        21 to Muscles(21, "Trapezium"),
        22 to Muscles(22, "Quadriceps"),
        23 to Muscles(23, "Hamstrings"),
        24 to Muscles(24, "Calves"),
        25 to Muscles(25, "Abs"),
        26 to Muscles(26, "Serratus"),
        27 to Muscles(27, "Glutes"),
        28 to Muscles(28, "Forearms"),
        29 to Muscles(29, "Hip Flexors"),
        30 to Muscles(30, "Adductors"),
        31 to Muscles(31, "Abductors"),
        32 to Muscles(32, "Hamstrings"),
        33 to Muscles(33, "Quadriceps"),
        34 to Muscles(34, "Calves"),
        35 to Muscles(35, "Obliques"),
        36 to Muscles(36, "Rhomboids"),
        37 to Muscles(37, "Pectorals"),
        38 to Muscles(38, "Levator Scapulae"),
        39 to Muscles(39, "Rotator Cuff"),
        40 to Muscles(40, "Triceps Brachii"),
        41 to Muscles(41, "Anterior Leg"),
        42 to Muscles(42, "Posterior Leg")
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
                profileImageUrl = "https://hips.hearstapps.com/hmg-prod/images/mh-trainer-2-1533576998.png?resize=640:*",
                contactEmail = "ibrahim.mohamed.coach@gmail.com",
                contactPhone = "01550162282",
                price = 250.00,
                rate = 4.6
            )
        )
    )

    private val exercises: Map<Int, Exercise> = mapOf(
        1 to Exercise(
            id = 1,
            name = "Incline Bench Press",
            description = "An exercise that targets the upper chest muscles using a bench set at an incline angle.",
            exerciseHint = "Dumbbell",
            exerciseSet = 3,
            exerciseReps = 8,
            exerciseIntensity = 90,
            categoryIds = listOf(1),
            effectedMusclesIds = listOf(2, 4),
            imageUrl = "",
            videoUrl = null
        ),
        2 to Exercise(
            id = 2,
            name = "Cable Flat Bench",
            description = "A flat bench press exercise performed using a cable machine to target the chest muscles.",
            exerciseHint = "Cable",
            exerciseSet = 3,
            exerciseReps = 8,
            exerciseIntensity = 90,
            categoryIds = listOf(1),
            effectedMusclesIds = listOf(3),
            imageUrl = "",
            videoUrl = null
        ),
        3 to Exercise(
            id = 3,
            name = "Push Up",
            description = "A bodyweight exercise that targets the chest, shoulders, and triceps using a raised surface for a deeper stretch.",
            exerciseHint = "Object to be able to get the long stretch",
            exerciseSet = 2,
            exerciseReps = 8,
            exerciseIntensity = 90,
            categoryIds = listOf(1),
            effectedMusclesIds = listOf(1),
            imageUrl = "",
            videoUrl = null
        ),
    )

    private val workouts: Map<Int, Workout> = mapOf(
        1 to Workout(
            id = 1,
            name = "Chest and Triceps",
            description = "A workout focused on building strength and endurance in the chest and triceps with various pressing and extension exercises.",
            durationMinutes = 60,
            exerciseIds = listOf(1, 2, 3),
            targetedMuscleIds = listOf(1, 10),
            coachId = 1,
            difficultyLevel = "Intermediate",
            imageUrl = "https://wallpapers.com/images/high/chris-bumstead-on-vacation-1lnwxxn436advkdz.webp",
            equipment = "Gym"
        ),
        2 to Workout(
            id = 2,
            name = "Back and Biceps",
            description = "A comprehensive workout targeting the back and biceps with exercises designed to enhance muscle growth and strength.",
            durationMinutes = 90,
            exerciseIds = listOf(4, 5, 6, 7),
            targetedMuscleIds = listOf(18, 20, 14),
            coachId = 1,
            difficultyLevel = "Intermediate",
            imageUrl = "https://wallpapers.com/images/high/chris-bumstead-flexing-back-muscles-w89ab209f4yxwd6a.webp",
            equipment = null
        )
    )

    private val trainPlans: Map<Int, TrainPlan> = mapOf(
        1 to TrainPlan(
            id = 1,
            name = "Low Volume High Intensity",
            description = "A training plan focused on high-intensity workouts with lower volume to maximize strength gains and muscle definition.",
            durationDaysPerTrainingWeek = 5,
            workoutsIds = listOf(1, 2),
            targetedMuscleIds = listOf(0),
            coachId = 1,
            difficultyLevel = "Intermediate",
            imageUrl = "https://image-cdn.essentiallysports.com/wp-content/uploads/Fi7aKrNXkAMXn9Y.jpg",
            trainingCategoriesIds = listOf(1),
            avgTimeMinPerWorkout = 100
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