package com.eibrahim67.gympro.core.data.local.source

import com.eibrahim67.gympro.core.data.local.model.User

class LocalDateSourceImpl(
    private val userDao: UserDao
) : LocalDateSource {
    override suspend fun addUser(user: User) = userDao.addUser(user)

    override suspend fun deleteLoggedInUser() = userDao.deleteLoggedInUser()

    override suspend fun getLoggedInUser() = userDao.getLoggedInUser()

    override suspend fun findLoggedInUser() = userDao.findLoggedInUser()

    override suspend fun logInUser(email: String) = userDao.logInUser(email)

    override suspend fun logOutUser() = userDao.logOutUser()

    override suspend fun getPassword(email: String) = userDao.getPassword(email)

    override suspend fun updatePassword(email: String, newPassword: String) =
        userDao.updatePassword(email, newPassword)

}