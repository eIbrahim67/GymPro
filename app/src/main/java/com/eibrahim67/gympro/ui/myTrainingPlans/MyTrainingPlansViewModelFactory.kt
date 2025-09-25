package com.eibrahim67.gympro.ui.myTrainingPlans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.domain.repository.RemoteRepository
import com.eibrahim67.gympro.domain.repository.UserRepository

class MyTrainingPlansViewModelFactory(
    private val remoteRepository: RemoteRepository, private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyTrainingPlansViewModel::class.java)) return MyTrainingPlansViewModel(
            remoteRepository,
            userRepository
        ) as T
        else throw IllegalArgumentException("Unknown view model")
    }
}