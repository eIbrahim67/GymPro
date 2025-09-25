package com.eibrahim67.gympro.ui.myWorkouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.domain.repository.RemoteRepository
import com.eibrahim67.gympro.domain.repository.UserRepository

class MyWorkoutsViewModelFactory(
    private val remoteRepository: RemoteRepository, private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyWorkoutsViewModel::class.java)) return MyWorkoutsViewModel(
            remoteRepository,
            userRepository
        ) as T
        else throw IllegalArgumentException("Unknown view model")
    }
}