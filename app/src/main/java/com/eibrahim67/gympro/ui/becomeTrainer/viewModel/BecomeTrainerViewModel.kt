package com.eibrahim67.gympro.ui.becomeTrainer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.data.remote.model.Category
import com.eibrahim67.gympro.data.remote.model.Coach
import com.eibrahim67.gympro.domain.repository.RemoteRepository
import com.eibrahim67.gympro.utils.UtilsFunctions.applyResponse
import com.eibrahim67.gympro.utils.response.ResponseEI

class BecomeTrainerViewModel(
    private val remoteRepository: RemoteRepository,
) : ViewModel() {


    private val _categories = MutableLiveData<ResponseEI<List<Category>>>()
    val categories: LiveData<ResponseEI<List<Category>>> get() = _categories
    fun getAllCategories() {
        applyResponse(_categories, viewModelScope) {
            remoteRepository.getAllCategories()
        }
    }

    private val _addedCoach = MutableLiveData<ResponseEI<Unit>>()
    val addedCoach: LiveData<ResponseEI<Unit>> get() = _addedCoach

    fun addCoach(coach: Coach) {
        applyResponse(_addedCoach, viewModelScope) {
            remoteRepository.addCoach(coach)
        }
    }

}