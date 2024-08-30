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

    override suspend fun logInUser(id: Int) = localDateSource.logInUser(id)

    override suspend fun logOutUser() = localDateSource.logOutUser()

    override suspend fun getPassword(id: Int) = localDateSource.getPassword(id)

    override suspend fun updatePassword(id: Int, newPassword: String) =
        localDateSource.updatePassword(id, newPassword)
}