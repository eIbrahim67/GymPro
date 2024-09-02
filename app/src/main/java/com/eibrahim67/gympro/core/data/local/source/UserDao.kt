package com.eibrahim67.gympro.core.data.local.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eibrahim67.gympro.core.data.local.model.User
import com.eibrahim67.gympro.home.model.TrainPlan

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("DELETE FROM USER WHERE  isLoggedIn = 1")
    suspend fun deleteLoggedInUser()

    @Query("SELECT * FROM user WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getLoggedInUser(): User?

    @Query("SELECT isLoggedIn FROM user WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun findLoggedInUser(): Boolean

    @Query("UPDATE user SET isLoggedIn = 1 WHERE email = :email")
    suspend fun logInUser(email: String)

    @Query("UPDATE user SET isLoggedIn = 0 WHERE isLoggedIn = 1")
    suspend fun logOutUser()

    @Query("SELECT password FROM user WHERE email = :email LIMIT 1")
    suspend fun getPassword(email: String): String?

    @Query("UPDATE user SET password = :newPassword WHERE email = :email")
    suspend fun updatePassword(email: String, newPassword: String)

    @Query("SELECT trainPlanId FROM user WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getTrainPlan(): Int?

    @Query("SELECT haveCoach FROM user WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun isLoggedInUserHaveTrainer(): Boolean
}