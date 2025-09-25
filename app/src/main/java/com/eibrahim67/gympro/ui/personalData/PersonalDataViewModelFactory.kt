package com.eibrahim67.gympro.ui.personalData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eibrahim67.gympro.domain.repository.UserRepository

class PersonalDataViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonalDataViewModel::class.java))
            return PersonalDataViewModel(userRepository) as T
        else
            throw IllegalArgumentException("Unknown view model")
    }
}