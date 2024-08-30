package com.eibrahim67.gympro.core.data.local.source

import com.eibrahim67.gympro.core.data.local.model.User

class LocalDateSourceImpl(
    private val userDao: UserDao
) : LocalDateSource {
    override suspend fun addUser(user: User) = userDao.addUser(user)

    override suspend fun deleteLoggedInUser() = userDao.deleteLoggedInUser()

    override suspend fun getLoggedInUser() = userDao.getLoggedInUser()

    override suspend fun logInUser(id: Int) = userDao.logInUser(id)

    override suspend fun logOutUser() = userDao.logOutUser()

    override suspend fun getPassword(id: Int) = userDao.getPassword(id)

    override suspend fun updatePassword(id: Int, newPassword: String) =
        userDao.updatePassword(id, newPassword)

}