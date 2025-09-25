package com.eibrahim67.gympro.ui.auth.signup.viewModel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eibrahim67.gympro.data.local.model.User
import com.eibrahim67.gympro.domain.repository.UserRepository
import com.eibrahim67.gympro.ui.auth.signup.model.ValidateCredentials
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class SignupViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _nameMessage = MutableLiveData<ValidateCredentials>()
    val nameMessage: LiveData<ValidateCredentials> = _nameMessage

    private val _usernameMessage = MutableLiveData<ValidateCredentials>()
    val usernameMessage: LiveData<ValidateCredentials> = _usernameMessage

    private val _phoneMessage = MutableLiveData<ValidateCredentials>()
    val phoneMessage: LiveData<ValidateCredentials> = _phoneMessage

    private val _emailMessage = MutableLiveData<ValidateCredentials>()
    val emailMessage: LiveData<ValidateCredentials> = _emailMessage

    private val _passwordMessage = MutableLiveData<ValidateCredentials>()
    val passwordMessage: LiveData<ValidateCredentials> = _passwordMessage

    private val _confirmPasswordMessage = MutableLiveData<ValidateCredentials>()
    val confirmPasswordMessage: LiveData<ValidateCredentials> = _confirmPasswordMessage


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

    fun registerUserFirebase(
        name: String, email: String, password: String, onResult: (Boolean) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    viewModelScope.launch {
                        if (userRepository.checkOnId(uid) == null) {
                            registerUser(
                                id = uid,
                                email = email,
                                password = password,
                            )
                            Log.i("signupRoomUser", "case1")
                        } else {
                            userRepository.logInUser(email)
                            Log.i("signupRoomUser", "case2")
                        }
                    }

                    val user = hashMapOf(
                        "name" to name, "email" to email
                    )
                    FirebaseFirestore.getInstance().collection("users").document(uid).set(user)
                    onResult(true)
                } else onResult(false)
            }
    }



    fun validateName(name: String) {
        _nameMessage.value = when {
            name.isEmpty() -> ValidateCredentials.InValid("First name cannot be empty")

            else -> ValidateCredentials.Valid
        }
    }

    fun validatePhone(phone: String) {
        _phoneMessage.value = when {
            phone.isEmpty() -> ValidateCredentials.InValid("Phone cannot be empty")
            phone.length < 11 -> ValidateCredentials.InValid("Phone number should be 11 number long")
            phone.length > 11 -> ValidateCredentials.InValid("Phone number should be 11 number long")

            else -> ValidateCredentials.Valid
        }
    }

    fun validateUsername(username: String) {
        _usernameMessage.value = when {
            username.isEmpty() -> ValidateCredentials.InValid("Username cannot be empty")
            username.length < 6 -> ValidateCredentials.InValid("Minimum 6 characters long")

            else -> ValidateCredentials.Valid
        }
    }

    fun validateEmail(email: String) {
        _emailMessage.value = when {
            email.isEmpty() -> ValidateCredentials.InValid("Email cannot be empty")
            !Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() -> ValidateCredentials.InValid("Invalid email address")

            else -> ValidateCredentials.Valid
        }
    }

    fun validatePassword(password: String) {
        _passwordMessage.value = when {
            password.isEmpty() -> ValidateCredentials.InValid("Password cannot be empty")
            password.length < 8 -> ValidateCredentials.InValid("Minimum 8 characters long")
            !password.matches(".*[0-9].*".toRegex()) -> ValidateCredentials.InValid("Minimum one number")
            !password.matches(".*[A-Z].*".toRegex()) -> ValidateCredentials.InValid("Minimum one uppercase letter")
            !password.matches(".*[a-z].*".toRegex()) -> ValidateCredentials.InValid("Minimum one lowercase letter")

            else -> ValidateCredentials.Valid
        }
    }

    fun validatePasswordConfirmation(password: String, confirmPassword: String) {
        _confirmPasswordMessage.value = when {
            confirmPassword.isEmpty() -> ValidateCredentials.InValid("Confirmation cannot be empty")
            confirmPassword != password -> ValidateCredentials.InValid("Passwords do not match")

            else -> ValidateCredentials.Valid
        }
    }

    fun validateCredentials(
        isFirstnameValid: Boolean,
        isUsernameValid: Boolean,
        isPhoneValid: Boolean,
        isEmailValid: Boolean,
        isPasswordValid: Boolean,
        isConfirmPasswordValid: Boolean
    ): ValidateCredentials {
        return when {
            !isUsernameValid && !isEmailValid && !isPasswordValid && !isConfirmPasswordValid && !isFirstnameValid && !isPhoneValid -> {

                ValidateCredentials.Valid
            }

            else -> {
                ValidateCredentials.InValid("Please fill in all fields")
            }
        }
    }


}