package com.eibrahim67.gympro.auth.splash.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse

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