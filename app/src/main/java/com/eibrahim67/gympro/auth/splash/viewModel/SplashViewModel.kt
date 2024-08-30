package com.eibrahim67.gympro.auth.splash.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse

class SplashViewModel(

    private val userRepository: UserRepository

) : ViewModel() {

    private val _thereIsLoggedInUser = MutableLiveData<Response<Boolean>>()
    val thereIsLoggedInUser: LiveData<Response<Boolean>> get() = _thereIsLoggedInUser

    fun thereIsLoggedInUser() {

        applyResponse(
            _thereIsLoggedInUser,
            viewModelScope
        ) {
            userRepository.findLoggedInUser()
        }

    }

}