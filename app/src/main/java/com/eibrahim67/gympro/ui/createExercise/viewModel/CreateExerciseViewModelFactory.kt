package com.eibrahim67.gympro.ui.createExercise.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.domain.repository.RemoteRepository
import com.eibrahim67.gympro.domain.repository.UserRepository

class CreateExerciseViewModelFactory(
    private val remoteRepository: RemoteRepository, private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateExerciseViewModel::class.java)) return CreateExerciseViewModel(
            remoteRepository,
            userRepository
        ) as T
        else throw IllegalArgumentException("Unknown view model")
    }
}