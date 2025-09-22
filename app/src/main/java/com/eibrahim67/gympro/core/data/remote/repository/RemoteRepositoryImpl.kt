package com.eibrahim67.gympro.core.data.remote.repository

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

    override suspend fun getMusclesByIds(ids: List<Int>): List<Muscles>? {
        return remoteDataSource.getMusclesByIds(ids)
    }

    override suspend fun getAllMuscles(): List<Muscles?> {
        return remoteDataSource.getAllMuscles()
    }

    override suspend fun addExercises(exercises: Exercise) {
        remoteDataSource.addExercises(exercises)
    }

    override suspend fun getExerciseById(id: Int): Exercise? {
        return remoteDataSource.getExerciseById(id)
    }

    override suspend fun getExercisesByIds(ids: List<Int>): List<Exercise>? {
        return remoteDataSource.getExercisesByIds(ids)
    }

    override suspend fun getAllExercises(): List<Exercise?> {
        return remoteDataSource.getAllExercises()
    }

    override suspend fun getMyExercisesIds(id: Int): List<Int>? {
        return remoteDataSource.getMyExercisesIds(id)
    }

    override suspend fun addExerciseId(coachId: Int, newExerciseId: Int) {
        remoteDataSource.addExerciseId(coachId, newExerciseId)
    }

    override suspend fun deleteExercise(id: Int) {
        remoteDataSource.deleteExercise(id)
    }

    override suspend fun addWorkouts(workouts: Workout) {
        remoteDataSource.addWorkouts(workouts)
    }

    override suspend fun getWorkoutById(id: Int): Workout? {
        return remoteDataSource.getWorkoutById(id)
    }

    override suspend fun getWorkoutsByIds(ids: List<Int>): List<Workout>? {
        return remoteDataSource.getWorkoutsByIds(ids)
    }

    override suspend fun getAllWorkouts(): List<Workout> {
        return remoteDataSource.getAllWorkouts()
    }

    override suspend fun getMyWorkoutsIds(id: Int): List<Int>? {
        return remoteDataSource.getMyWorkoutsIds(id)
    }

    override suspend fun addWorkoutId(coachId: Int, newWorkoutId: Int) {
        remoteDataSource.addWorkoutId(coachId, newWorkoutId)
    }

    override suspend fun deleteWorkout(id: Int) {
        remoteDataSource.deleteWorkout(id)
    }

    override suspend fun addTrainPlanId(coachId: Int, newPlanId: Int) {
        remoteDataSource.addTrainPlanId(coachId, newPlanId)
    }

    override suspend fun addTrainPlans(trainPlans: TrainPlan) {
        remoteDataSource.addTrainPlans(trainPlans)
    }

    override suspend fun getTrainPlanById(id: Int): TrainPlan? {
        return remoteDataSource.getTrainPlanById(id)
    }

    override suspend fun getTrainPlanByIds(ids: List<Int>): List<TrainPlan>? {
        return remoteDataSource.getTrainPlanByIds(ids)
    }

    override suspend fun getAllTrainPlans(): List<TrainPlan> {
        return remoteDataSource.getAllTrainPlans()
    }

    override suspend fun getMyTrainPlansIds(id: Int): List<Int>? {
        return remoteDataSource.getMyTrainPlansIds(id)
    }

    override suspend fun deleteTrainPlan(id: Int) {
        remoteDataSource.deleteTrainPlan(id)
    }


    override suspend fun addCoach(coach: Coach) = remoteDataSource.addCoach(coach)

    override suspend fun getCoachById(id: String): Coach? {
        return remoteDataSource.getCoachById(id)
    }

    override suspend fun addCategory(categories: Map<Int, Category>) {
        remoteDataSource.addCategories(categories)
    }

    override suspend fun getAllCategories(): List<Category> {
        return remoteDataSource.getAllCategories()
    }

    override suspend fun getAllCoaches(): List<Coach> {
        return remoteDataSource.getAllCoaches()
    }

}