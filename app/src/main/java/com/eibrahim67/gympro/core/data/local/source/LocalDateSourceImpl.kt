package com.eibrahim67.gympro.core.data.local.source

import com.eibrahim67.gympro.core.data.local.model.User

class LocalDateSourceImpl(
    private val userDao: UserDao
) : LocalDateSource {
    override suspend fun addUser(user: User) = userDao.addUser(user)

    override suspend fun deleteLoggedInUser() = userDao.deleteLoggedInUser()

    override suspend fun getLoggedInUser() = userDao.getLoggedInUser()

    override suspend fun updateName(name: String) = userDao.updateName(name)

    override suspend fun updatePhone(phone: String) = userDao.updatePhone(phone)

    override suspend fun updateTypeBody(type: String) = userDao.updateTypeBody(type)

    override suspend fun findLoggedInUser() = userDao.findLoggedInUser()

    override suspend fun logInUser(email: String) = userDao.logInUser(email)

    override suspend fun logOutUser() = userDao.logOutUser()

    override suspend fun getPassword(email: String) = userDao.getPassword(email)

    override suspend fun updatePassword(email: String, newPassword: String) =
        userDao.updatePassword(email, newPassword)

    override suspend fun getTrainPlanId() = userDao.getTrainPlan()

    override suspend fun isLoggedInUserHaveTrainer() = userDao.isLoggedInUserHaveTrainer()
    override suspend fun getUserExerciseData() = userDao.getUserExerciseData()

    override suspend fun updateUserExerciseData(data: Map<Int, MutableList<String>>) =
        userDao.updateUserExerciseData(data)

    override suspend fun updateHaveCoach(data: Boolean) = userDao.updateHaveCoach(data)

    override suspend fun updateTrainPlanId(data: Int?) = userDao.updateTrainPlanId(data)
    override suspend fun getUserTrainPlanId(): Int? = userDao.getUserTrainPlanId()


}