package com.eibrahim67.gympro.ui.auth.splash.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.domain.repository.UserRepository
import com.eibrahim67.gympro.utils.UtilsFunctions.applyResponse
import com.eibrahim67.gympro.utils.response.ResponseEI

class SplashViewModel(

    private val userRepository: UserRepository

) : ViewModel() {

    private val _thereIsLoggedInUser = MutableLiveData<ResponseEI<Boolean>>()
    val thereIsLoggedInUser: LiveData<ResponseEI<Boolean>> get() = _thereIsLoggedInUser

    fun thereIsLoggedInUser() {

        applyResponse(
            _thereIsLoggedInUser,
            viewModelScope
        ) {
            userRepository.findLoggedInUser()
        }

    }

}