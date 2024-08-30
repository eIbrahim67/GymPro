package com.eibrahim67.gympro.core.data.local.repository

import com.eibrahim67.gympro.core.data.local.model.User

interface UserRepository {

    suspend fun addUser(user: User)

    suspend fun deleteLoggedInUser()

    suspend fun getLoggedInUser(): User?

    suspend fun findLoggedInUser(): Boolean

    suspend fun logInUser(email: String)

    suspend fun logOutUser()

    suspend fun getPassword(email: String): String?

    suspend fun updatePassword(email: String, newPassword: String)

}