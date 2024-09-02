package com.eibrahim67.gympro.core.data.local.repository

import com.eibrahim67.gympro.core.data.local.model.User
import com.eibrahim67.gympro.core.data.local.source.LocalDateSource

class UserRepositoryImpl(
    private val localDateSource: LocalDateSource
) : UserRepository {
    override suspend fun addUser(user: User) = localDateSource.addUser(user)

    override suspend fun deleteLoggedInUser() = localDateSource.deleteLoggedInUser()

    override suspend fun getLoggedInUser() = localDateSource.getLoggedInUser()

    override suspend fun findLoggedInUser() = localDateSource.findLoggedInUser()

    override suspend fun logInUser(email: String) = localDateSource.logInUser(email)

    override suspend fun logOutUser() = localDateSource.logOutUser()

    override suspend fun getPassword(email: String) = localDateSource.getPassword(email)

    override suspend fun updatePassword(email: String, newPassword: String) =
        localDateSource.updatePassword(email, newPassword)

    override suspend fun getTrainPlanId() = localDateSource.getTrainPlanId()

    override suspend fun isLoggedInUserHaveTrainer() = localDateSource.isLoggedInUserHaveTrainer()


}