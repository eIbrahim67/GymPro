package com.eibrahim67.gympro.createExercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepository

class CreateExerciseViewModelFactory (
    private val remoteRepository: RemoteRepository,
    private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(CreateExerciseViewModel::class.java))
                return CreateExerciseViewModel(remoteRepository, userRepository) as T
            else
                throw IllegalArgumentException("Unknown view model")
        }
    }