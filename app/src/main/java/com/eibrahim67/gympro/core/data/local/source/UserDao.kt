package com.eibrahim67.gympro.core.data.local.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eibrahim67.gympro.core.data.local.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("DELETE FROM USER WHERE  isLoggedIn = 1")
    suspend fun deleteLoggedInUser()

    @Query("SELECT * FROM user WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getLoggedInUser(): User?

    @Query("UPDATE user SET isLoggedIn = 1 WHERE id = :id")
    suspend fun logInUser(id: Int)

    @Query("UPDATE user SET isLoggedIn = 0 WHERE isLoggedIn = 1")
    suspend fun logOutUser()

    @Query("SELECT password FROM user WHERE id = :id LIMIT 1")
    suspend fun getPassword(id: Int): String?

    @Query("UPDATE user SET password = :newPassword WHERE id = :id")
    suspend fun updatePassword(id: Int, newPassword : String)

}