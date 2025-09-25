package com.eibrahim67.gympro.data.local.repository

import com.eibrahim67.gympro.data.local.model.User
import com.eibrahim67.gympro.data.local.source.LocalDateSource
import com.eibrahim67.gympro.domain.repository.UserRepository

class UserRepositoryImpl(
    private val localDateSource: LocalDateSource
) : UserRepository {
    override suspend fun addUser(user: User) = localDateSource.addUser(user)

    override suspend fun deleteLoggedInUser() = localDateSource.deleteLoggedInUser()

    override suspend fun getLoggedInUser() = localDateSource.getLoggedInUser()

    override suspend fun checkOnId(id: String): User? = localDateSource.checkOnId(id)

    override suspend fun updateTypeBody(type: String) = localDateSource.updateTypeBody(type)

    override suspend fun findLoggedInUser() = localDateSource.findLoggedInUser()

    override suspend fun logInUser(email: String) = localDateSource.logInUser(email)

    override suspend fun logOutUser() = localDateSource.logOutUser()

    override suspend fun getPassword(email: String) = localDateSource.getPassword(email)

    override suspend fun updatePassword(email: String, newPassword: String) =
        localDateSource.updatePassword(email, newPassword)

    override suspend fun getTrainPlanId() = localDateSource.getTrainPlanId()

    override suspend fun isLoggedInUserHaveTrainer() = localDateSource.isLoggedInUserHaveTrainer()
    override suspend fun getUserExerciseData() = localDateSource.getUserExerciseData()

    override suspend fun updateUserExerciseData(data: Map<Int, MutableList<String>>) =
        localDateSource.updateUserExerciseData(data)

    override suspend fun updateHaveCoach(data: Boolean) = localDateSource.updateHaveCoach(data)

    override suspend fun updateTrainPlanId(data: Int?) = localDateSource.updateTrainPlanId(data)
    override suspend fun getUserTrainPlanId() = localDateSource.getUserTrainPlanId()
}