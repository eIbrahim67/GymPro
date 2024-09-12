package com.eibrahim67.gympro.core.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {

    @TypeConverter
    fun fromStringToList(value: String?): List<String> {
        return if (value.isNullOrEmpty()) {
            emptyList()
        } else {
            value.split(",").map { it.trim() }
        }
    }

    @TypeConverter
    fun fromListToString(list: List<String>?): String {
        return list?.filter { it.isNotEmpty() }?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun fromMap(map: Map<Int, MutableList<String>>): String = Gson().toJson(map)

    @TypeConverter
    fun toMap(value: String): Map<Int, MutableList<String>> {

        val mapType = object : TypeToken<Map<Int, MutableList<String>>>() {}.type

        return Gson().fromJson(value, mapType)

    }

}