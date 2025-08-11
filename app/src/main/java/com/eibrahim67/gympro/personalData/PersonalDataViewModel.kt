package com.eibrahim67.gympro.personalData

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.core.data.local.model.User
import com.eibrahim67.gympro.core.data.local.repository.UserRepository
import com.eibrahim67.gympro.core.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions.applyResponse


class PersonalDataViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _personalData = MutableLiveData<ResponseEI<User?>>()
    val personalData: LiveData<ResponseEI<User?>> get() = _personalData

    fun loadPersonalData() {
        applyResponse(
            _personalData,
            viewModelScope
        ) {
            userRepository.getLoggedInUser()
        }

    }

    private val _updateName = MutableLiveData<ResponseEI<Unit>>()
    val updateName: LiveData<ResponseEI<Unit>> get() = _updateName
    fun updateName(name: String?) {
        if (!name.isNullOrEmpty()) {
            applyResponse(
                _updateName,
                viewModelScope
            ) {
                userRepository.updateName(name)
            }
        }
    }

    private val _updatePhone = MutableLiveData<ResponseEI<Unit>>()
    val updatePhone: LiveData<ResponseEI<Unit>> get() = _updatePhone
    fun updatePhone(phone: String?) {
        if (!phone.isNullOrEmpty()) {
            applyResponse(
                _updatePhone,
                viewModelScope
            ) {
                userRepository.updatePhone(phone)
            }
        }
    }

    private val _updateTypeBody = MutableLiveData<ResponseEI<Unit>>()
    val updateTypeBody: LiveData<ResponseEI<Unit>> get() = _updateTypeBody
    fun updateTypeBody(type: String?) {
        Log.e("test", type.toString())
        if (!type.isNullOrEmpty()) {
            applyResponse(
                _updateTypeBody,
                viewModelScope
            ) {
                userRepository.updateTypeBody(type)
            }
        }
    }
}
