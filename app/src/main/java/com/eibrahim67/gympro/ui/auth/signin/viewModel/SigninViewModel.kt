package com.eibrahim67.gympro.ui.auth.signin.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.domain.repository.UserRepository
import com.eibrahim67.gympro.ui.auth.signup.model.ValidateCredentials
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SigninViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isUserValid = MutableLiveData<ValidateCredentials?>()
    val isUserValid: LiveData<ValidateCredentials?> = _isUserValid

    fun checkUser(email: String, password: String) {
        viewModelScope.launch {
            when (val foundPassword = userRepository.getPassword(email)) {
                null -> _isUserValid.value = ValidateCredentials.InValid("User not found")

                else -> validateUser(password, foundPassword, email)
            }
        }
    }

    val auth = FirebaseAuth.getInstance()

    fun loginUser(email: String, password: String, onResult: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }.addOnSuccessListener {
                Log.i("loginUserFirebase", "Success")
            }.addOnFailureListener { e ->
                Log.i("loginUserFirebase", e.toString())

            }
    }


    private suspend fun validateUser(password: String, realPassword: String, email: String) {

        when (password == realPassword) {
            true -> {
                _isUserValid.value = ValidateCredentials.Valid
                userRepository.logInUser(email)
            }

            false -> {
                _isUserValid.value = ValidateCredentials.InValid("Incorrect Credentials")
            }
        }
    }

    fun resetStates() {
        _isUserValid.value = null
    }

}