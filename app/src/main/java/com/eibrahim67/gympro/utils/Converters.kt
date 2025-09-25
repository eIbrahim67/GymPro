package com.eibrahim67.gympro.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {

    @TypeConverter
    fun fromMap(map: Map<Int, MutableList<String>>): String = Gson().toJson(map)

    @TypeConverter
    fun toMap(value: String): Map<Int, MutableList<String>> {

        val mapType = object : TypeToken<Map<Int, MutableList<String>>>() {}.type

        return Gson().fromJson(value, mapType)

    }

}