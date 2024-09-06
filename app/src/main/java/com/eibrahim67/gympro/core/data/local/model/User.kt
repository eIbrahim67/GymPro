package com.eibrahim67.gympro.core.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.eibrahim67.gympro.core.utils.Converters

@Entity(

    tableName = "user",

    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["phone"], unique = true),
        Index(value = ["username"], unique = true)
    ],

    )

data class User(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "phone")
    val phone: String,

    @ColumnInfo(name = "typeBody")
    val typeBody: String? = null,

    @ColumnInfo(name = "isLoggedIn")
    val isLoggedIn: Boolean = false,

    @ColumnInfo(name = "isSubscribed")
    val isSubscribed: Boolean = false,

    @ColumnInfo(name = "haveCoach")
    val haveCoach: Boolean = false,

    @ColumnInfo(name = "trainPlanId")
    val trainPlanId: Int? = null,

    @ColumnInfo(name = "userExerciseData")
    val userExerciseData: Map<Int, Map<String, String>> = emptyMap()

)
