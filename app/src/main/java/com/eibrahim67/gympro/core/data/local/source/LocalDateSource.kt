package com.eibrahim67.gympro.core.data.local.source

import com.eibrahim67.gympro.core.data.local.model.User

interface LocalDateSource {

    suspend fun addUser(user: User)

    suspend fun deleteLoggedInUser()

    suspend fun getLoggedInUser(): User?

    suspend fun logInUser(id: Int)

    suspend fun logOutUser()

    suspend fun getPassword(id: Int): String?

    suspend fun updatePassword(id: Int, newPassword: String)

}