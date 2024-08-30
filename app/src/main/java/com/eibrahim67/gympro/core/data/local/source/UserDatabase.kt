package com.eibrahim67.gympro.core.data.local.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eibrahim67.gympro.core.data.local.model.User

@Database(
    entities = [
        User::class
    ], version = 1
)

abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabaseInstance(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    klass = UserDatabase::class.java,
                    name = "gym_pro_database"
                ).build().also { INSTANCE = it }
            }
        }

    }

}