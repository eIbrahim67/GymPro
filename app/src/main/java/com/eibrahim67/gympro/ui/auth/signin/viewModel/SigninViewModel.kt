package com.eibrahim67.gympro.ui.auth.signin.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.data.local.model.User
import com.eibrahim67.gympro.domain.repository.UserRepository
import com.eibrahim67.gympro.ui.auth.signup.model.ValidateCredentials
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SigninViewModel(
    private val userRepository: UserRepository
) : ViewModel() {


    private val _isUserValid = MutableLiveData<ValidateCredentials?>()
    val isUserValid: LiveData<ValidateCredentials?> = _isUserValid


    fun registerUser(
        id: String, email: String, password: String
    ) {
        val createdUser = User(
            uid = id, email = email, password = password, isLoggedIn = true
        )
        addUser(createdUser)
    }

    private fun addUser(user: User) {
        viewModelScope.launch {
            userRepository.addUser(user)
        }.invokeOnCompletion {
            _isUserValid.value = ValidateCredentials.Valid
        }
    }

    val auth = FirebaseAuth.getInstance()

    fun loginUser(email: String, password: String, onResult: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            onResult(task.isSuccessful)
        }.addOnSuccessListener {
            Log.i("loginUserFirebase", "Success")

            val uid = auth.currentUser?.uid ?: return@addOnSuccessListener

            viewModelScope.launch {
                if (userRepository.checkOnId(uid) == null) {
                    registerUser(
                        id = uid,
                        email = email,
                        password = password,
                    )
                    Log.i("loginRoomUser", "case1")
                } else {
                    userRepository.logInUser(email)
                    Log.i("loginRoomUser", "case2")
                    _isUserValid.value = ValidateCredentials.Valid
                }
            }
        }.addOnFailureListener { e ->
            Log.i("loginUserFirebase", e.toString())

        }
    }
}