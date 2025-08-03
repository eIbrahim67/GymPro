package com.eibrahim67.gympro.core.data.local.preference

import android.content.Context
import androidx.core.content.edit
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PreferenceManager(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences("workout_prefs", Context.MODE_PRIVATE)

    // Keys
    companion object {
        private const val KEY_DAY_NUMBER = "day_number"
        private const val KEY_WORKOUT_NUMBER = "workout_number"
        private const val KEY_WORKOUT_DONE = "workout_done"
        private const val KEY_EXERCISES_NUMBER = "exercises_number"
        private const val KEY_REPS_NUMBER = "reps_number"
        private const val KEY_TIME_SPENT = "time_spent"
    }

    // --------- Getters ---------
    fun getDayNumber(): Int = sharedPreferences.getInt(KEY_DAY_NUMBER, -1)
    fun getWorkoutNumber(len: Int): Int {
        var workoutNumber = sharedPreferences.getInt(KEY_WORKOUT_NUMBER, 0)
        if (workoutNumber == len) {
            workoutNumber = 0
            setWorkoutNumber(0)
        }
        return workoutNumber
    }

    fun getWorkoutDone(): Boolean = sharedPreferences.getBoolean(KEY_WORKOUT_DONE, false)
    fun getExercisesNumber(): Int = sharedPreferences.getInt(KEY_EXERCISES_NUMBER, 0)
    fun getRepsNumber(): Int = sharedPreferences.getInt(KEY_REPS_NUMBER, 0)
    fun getTimeSpent(): Int = sharedPreferences.getInt(KEY_TIME_SPENT, 0)

    // --------- Setters ---------
    fun setDayNumber(value: Int) = sharedPreferences.edit().putInt(KEY_DAY_NUMBER, value).apply()
    fun setWorkoutNumber(value: Int) =
        sharedPreferences.edit().putInt(KEY_WORKOUT_NUMBER, value).apply()

    fun setWorkoutDone(value: Boolean) =
        sharedPreferences.edit().putBoolean(KEY_WORKOUT_DONE, value).apply()

    fun setExercisesNumber(value: Int) =
        sharedPreferences.edit().putInt(KEY_EXERCISES_NUMBER, value).apply()

    fun setRepsNumber(value: Int) = sharedPreferences.edit().putInt(KEY_REPS_NUMBER, value).apply()
    fun setTimeSpent(value: Int) = sharedPreferences.edit().putInt(KEY_TIME_SPENT, value).apply()

    // --------- Increasers ---------

    fun incrementWorkoutDone(by: Int = 1, len: Int) {
        setWorkoutNumber(getWorkoutNumber(len) + by)
    }

    fun incrementExercisesDone(by: Int = 1) {
        setExercisesNumber(getExercisesNumber() + by)
    }

    fun incrementRepsDone(by: Int = 1) {
        setRepsNumber(getRepsNumber() + by)
    }

    fun incrementTimeSpent(by: Int = 1) {
        setTimeSpent(getTimeSpent() + by)
    }

    // --------- Reset ---------
    fun resetAllProgress() {
        sharedPreferences.edit()
            .putInt(KEY_DAY_NUMBER, -1)
            .putInt(KEY_WORKOUT_NUMBER, 0)
            .putBoolean(KEY_WORKOUT_DONE, false)
            .putInt(KEY_EXERCISES_NUMBER, 0)
            .putInt(KEY_REPS_NUMBER, 0)
            .putInt(KEY_TIME_SPENT, 0)
            .apply()
    }

    fun newDay(len: Int): Int {

        sharedPreferences.edit {
            putInt(KEY_DAY_NUMBER, getTodayKey())
        }

        if (getWorkoutDone()) {
            incrementWorkoutDone(len=len)
            sharedPreferences.edit {
                putInt(KEY_EXERCISES_NUMBER, 0)
                    .putBoolean(KEY_WORKOUT_DONE, false)
                    .putInt(KEY_REPS_NUMBER, 0)
                    .putInt(KEY_TIME_SPENT, 0)
            }
            return 0
        }
        if (getTimeSpent() > 0)
            return 1
        return 2
    }

    fun finishWorkout(len: Int) {

        incrementWorkoutDone(len=len)
        sharedPreferences.edit()
            .putInt(KEY_DAY_NUMBER, getTodayKey())
            .putInt(KEY_EXERCISES_NUMBER, 0)
            .putBoolean(KEY_WORKOUT_DONE, false)
            .putInt(KEY_REPS_NUMBER, 0)
            .putInt(KEY_TIME_SPENT, 0)
            .apply()

    }

    fun reTrainWorkout() {

        sharedPreferences.edit()
            .putInt(KEY_DAY_NUMBER, getTodayKey())
            .putInt(KEY_EXERCISES_NUMBER, 0)
            .putBoolean(KEY_WORKOUT_DONE, false)
            .putInt(KEY_REPS_NUMBER, 0)
            .putInt(KEY_TIME_SPENT, 0)
            .apply()

    }

    fun getTodayKey(): Int {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return dateFormat.format(Date()).toInt()
    }

}
