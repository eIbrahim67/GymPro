package com.eibrahim67.gympro.train.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse

class TrainViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userHaveTrainer = MutableLiveData<ResponseEI<Boolean>>()

    val userHaveTrainer: LiveData<ResponseEI<Boolean>> get() = _userHaveTrainer

    fun isUserHaveTrainer() = applyResponse(
        _userHaveTrainer,
        viewModelScope
    ) { userRepository.isLoggedInUserHaveTrainer() }

}