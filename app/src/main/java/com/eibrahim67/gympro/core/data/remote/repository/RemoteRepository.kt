package com.eibrahim67.gympro.core.data.remote.repository

import androidx.lifecycle.LiveData
import com.eibrahim67.gympro.core.data.remote.model.Category
import com.eibrahim67.gympro.core.data.remote.model.Coach
import com.eibrahim67.gympro.core.data.remote.model.Exercise
import com.eibrahim67.gympro.core.data.remote.model.Muscles
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.model.Workout

interface RemoteRepository {
    suspend fun addCategory(categories: Map<Int, Category>)

    suspend fun addMuscle(muscles: Map<Int, Muscles>)

    suspend fun addCoach(coach: Map<Int, Coach>)

    suspend fun addExercise(exercise: Map<Int, Exercise>)

    suspend fun addWorkout(workout: Map<Int, Workout>)

    suspend fun addTrainPlan(trainPlan: Map<Int, TrainPlan>)

    suspend fun getCategories(): LiveData<List<Category>>
}