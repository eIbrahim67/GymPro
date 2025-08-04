package com.eibrahim67.gympro.core.data.remote.repository

import androidx.lifecycle.LiveData
import com.eibrahim67.gympro.core.data.remote.model.Category
import com.eibrahim67.gympro.core.data.remote.model.Coach
import com.eibrahim67.gympro.core.data.remote.model.Exercise
import com.eibrahim67.gympro.core.data.remote.model.Muscles
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.model.Workout

interface RemoteRepository {

    suspend fun addMuscles(muscles: Map<Int, Muscles>)
    suspend fun getMuscleById(id: Int): Muscles?
    suspend fun getAllMuscles(): Map<Int, Muscles>

    suspend fun addExercises(exercises: Map<Int, Exercise>)
    suspend fun getExerciseById(id: Int): Exercise?
    suspend fun getAllExercises(): Map<Int, Exercise>

    suspend fun addWorkouts(workouts: Map<Int, Workout>)
    suspend fun getWorkoutById(id: Int): Workout?
    suspend fun getAllWorkouts(): Map<Int, Workout>

    suspend fun addTrainPlans(trainPlans: TrainPlan)
    suspend fun getTrainPlanById(id: Int): TrainPlan?
    suspend fun getAllTrainPlans(): Map<Int, TrainPlan>
    suspend fun getMyTrainPlans(id: Int): List<String>?

    suspend fun addCategory(categories: Map<Int, Category>)
    suspend fun getAllCategories(): Map<Int, Category>


    suspend fun addCoach(coach: Map<Int, Coach>)
}
