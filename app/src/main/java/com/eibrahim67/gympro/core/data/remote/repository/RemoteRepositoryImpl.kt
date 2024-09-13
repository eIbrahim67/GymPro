package com.eibrahim67.gympro.core.data.remote.repository

import androidx.lifecycle.LiveData
import com.eibrahim67.gympro.core.data.remote.model.Category
import com.eibrahim67.gympro.core.data.remote.model.Coach
import com.eibrahim67.gympro.core.data.remote.model.Exercise
import com.eibrahim67.gympro.core.data.remote.model.Muscles
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.model.Workout
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSource

class RemoteRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : RemoteRepository {
    override suspend fun addCategory(categories: Map<Int, Category>) =
        remoteDataSource.addCategory(categories)

    override suspend fun addMuscle(muscles: Map<Int, Muscles>) = remoteDataSource.addMuscle(muscles)
    override suspend fun addCoach(coach: Map<Int, Coach>) = remoteDataSource.addCoach(coach)
    override suspend fun addExercise(exercise: Map<Int, Exercise>) =
        remoteDataSource.addExercise(exercise)

    override suspend fun addWorkout(workout: Map<Int, Workout>) =
        remoteDataSource.addWorkout(workout)

    override suspend fun addTrainPlan(trainPlan: Map<Int, TrainPlan>) =
        remoteDataSource.addTrainPlan(trainPlan)

    override suspend fun getCategories(): LiveData<List<Category>> = remoteDataSource.getCategories()

}