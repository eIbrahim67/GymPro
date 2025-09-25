package com.eibrahim67.gympro.domain.repository

import com.eibrahim67.gympro.data.remote.model.Category
import com.eibrahim67.gympro.data.remote.model.Coach
import com.eibrahim67.gympro.data.remote.model.Exercise
import com.eibrahim67.gympro.data.remote.model.Muscles
import com.eibrahim67.gympro.data.remote.model.TrainPlan
import com.eibrahim67.gympro.data.remote.model.Workout

interface RemoteRepository {

    suspend fun addMuscles(muscles: Map<Int, Muscles>)
    suspend fun getMuscleById(id: Int): Muscles?
    suspend fun getMusclesByIds(ids: List<Int>): List<Muscles>?
    suspend fun getAllMuscles(): List<Muscles?>

    suspend fun addExercises(exercises: Exercise)
    suspend fun getExerciseById(id: Int): Exercise?
    suspend fun getExercisesByIds(ids: List<Int>): List<Exercise>?
    suspend fun getAllExercises(): List<Exercise?>
    suspend fun getMyExercisesIds(id: String): List<Int>?
    suspend fun addExerciseId(coachId: String, newWExercisesId: Int)
    suspend fun deleteExercise(id: Int)

    suspend fun addWorkouts(workouts: Workout)
    suspend fun getWorkoutById(id: Int): Workout?
    suspend fun getWorkoutsByIds(ids: List<Int>): List<Workout>?
    suspend fun getAllWorkouts(): List<Workout>
    suspend fun getMyWorkoutsIds(id: String): List<Int>?
    suspend fun addWorkoutId(coachId: String, newWorkoutId: Int)
    suspend fun deleteWorkout(id: Int)

    suspend fun addTrainPlans(trainPlans: TrainPlan)
    suspend fun addTrainPlanId(coachId: String, newPlanId: Int)
    suspend fun getTrainPlanById(id: Int): TrainPlan?
    suspend fun getTrainPlanByIds(ids: List<Int>): List<TrainPlan>?
    suspend fun getAllTrainPlans(): List<TrainPlan>
    suspend fun getMyTrainPlansIds(id: String): List<Int>?
    suspend fun deleteTrainPlan(id: Int)

    suspend fun addCategory(categories: Map<Int, Category>)
    suspend fun getAllCategories(): List<Category>


    suspend fun addCoach(coach: Coach)
    suspend fun getCoachById(id: String) : Coach?
    suspend fun getAllCoaches(): List<Coach>

}