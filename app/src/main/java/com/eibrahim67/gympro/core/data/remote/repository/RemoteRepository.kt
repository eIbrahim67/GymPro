package com.eibrahim67.gympro.core.data.remote.repository

import com.eibrahim67.gympro.core.data.remote.model.Category
import com.eibrahim67.gympro.core.data.remote.model.Coach
import com.eibrahim67.gympro.core.data.remote.model.Exercise
import com.eibrahim67.gympro.core.data.remote.model.Muscles
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.model.Workout

interface RemoteRepository {

    suspend fun addMuscles(muscles: Map<Int, Muscles>)
    suspend fun getMuscleById(id: Int): Muscles?
    suspend fun getMusclesByIds(ids: List<Int>): List<Muscles>?
    suspend fun getAllMuscles(): List<Muscles?>

    suspend fun addExercises(exercises: Map<Int, Exercise>)
    suspend fun getExerciseById(id: Int): Exercise?
    suspend fun getExercisesByIds(ids: List<Int>): List<Exercise>?
    suspend fun getAllExercises(): Map<Int, Exercise>

    suspend fun addWorkouts(workouts: Map<Int, Workout>)
    suspend fun getWorkoutById(id: Int): Workout?
    suspend fun getWorkoutsByIds(ids: List<Int>): List<Workout>?
    suspend fun getAllWorkouts(): List<Workout>

    suspend fun addTrainPlans(trainPlans: TrainPlan)
    suspend fun addTrainPlanId(coachId: Int, newPlanId: Int)
    suspend fun getTrainPlanById(id: Int): TrainPlan?
    suspend fun getTrainPlanByIds(ids: List<Int>): List<TrainPlan>?
    suspend fun getAllTrainPlans(): List<TrainPlan>
    suspend fun getMyTrainPlansIds(id: Int): List<Int>?

    suspend fun addCategory(categories: Map<Int, Category>)
    suspend fun getAllCategories(): List<Category>


    suspend fun addCoach(coach: Coach)
    suspend fun getCoachById(id: Int) : Coach?
    suspend fun getAllCoaches(): List<Coach>

}
