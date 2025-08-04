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

    override suspend fun addMuscles(muscles: Map<Int, Muscles>) {
        remoteDataSource.addMuscles(muscles)
    }

    override suspend fun getMuscleById(id: Int): Muscles? {
        return remoteDataSource.getMuscleById(id)
    }

    override suspend fun getAllMuscles(): Map<Int, Muscles> {
        return remoteDataSource.getAllMuscles()
    }

    override suspend fun addExercises(exercises: Map<Int, Exercise>) {
        remoteDataSource.addExercises(exercises)
    }

    override suspend fun getExerciseById(id: Int): Exercise? {
        return remoteDataSource.getExerciseById(id)
    }

    override suspend fun getAllExercises(): Map<Int, Exercise> {
        return remoteDataSource.getAllExercises()
    }

    override suspend fun addWorkouts(workouts: Map<Int, Workout>) {
        remoteDataSource.addWorkouts(workouts)
    }

    override suspend fun getWorkoutById(id: Int): Workout? {
        return remoteDataSource.getWorkoutById(id)
    }

    override suspend fun getAllWorkouts(): Map<Int, Workout> {
        return remoteDataSource.getAllWorkouts()
    }

    override suspend fun addTrainPlans(trainPlans: TrainPlan) {
        remoteDataSource.addTrainPlans(trainPlans)
    }

    override suspend fun getTrainPlanById(id: Int): TrainPlan? {
        return remoteDataSource.getTrainPlanById(id)
    }

    override suspend fun getAllTrainPlans(): Map<Int, TrainPlan> {
        return remoteDataSource.getAllTrainPlans()
    }

    override suspend fun getMyTrainPlans(id: Int): List<String>? {
        return remoteDataSource.getMyTrainPlans(id)
    }

    override suspend fun addCoach(coach: Map<Int, Coach>) = remoteDataSource.addCoach(coach)

    override suspend fun addCategory(categories: Map<Int, Category>) {
        remoteDataSource.addCategories(categories)
    }

    override suspend fun getAllCategories(): Map<Int, Category> {
        return remoteDataSource.getAllCategories()
    }

}