package com.eibrahim67.gympro.auth.signup.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.auth.signup.model.ValidateCredentials
import com.eibrahim67.gympro.auth.signup.viewModel.SignupViewModel
import com.eibrahim67.gympro.auth.signup.viewModel.SignupViewModelFactory
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.utils.UtilsFunctions.createMaterialAlertDialogBuilderOk
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignupFragment : Fragment() {

    private lateinit var usernameField: TextInputEditText
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var emailField: TextInputEditText
    private lateinit var emailLayout: TextInputLayout
    private lateinit var fNameField: TextInputEditText
    private lateinit var fNameLayout: TextInputLayout
    private lateinit var lNameField: TextInputEditText
    private lateinit var phoneField: TextInputEditText
    private lateinit var phoneLayout: TextInputLayout
    private lateinit var passwordField: TextInputEditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var passwordConfirmField: TextInputEditText
    private lateinit var passwordConfirmLayout: TextInputLayout
    private lateinit var signUpButton: MaterialCardView

    private val viewModel: SignupViewModel by viewModels {
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        SignupViewModelFactory(userRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)

        initListeners()

        observeValidations()
    }

    private fun initViews(view: View) {
        usernameField = view.findViewById(R.id.usernameField)
        usernameLayout = view.findViewById(R.id.usernameLayout)
        emailField = view.findViewById(R.id.emailField)
        emailLayout = view.findViewById(R.id.emailLayout)
        fNameField = view.findViewById(R.id.fNameField)
        fNameLayout = view.findViewById(R.id.fNameLayout)
        lNameField = view.findViewById(R.id.lNameField)
        phoneField = view.findViewById(R.id.phoneField)
        phoneLayout = view.findViewById(R.id.phoneLayout)
        passwordField = view.findViewById(R.id.passwordField)
        passwordLayout = view.findViewById(R.id.passwordLayout)
        passwordConfirmField = view.findViewById(R.id.passwordConfirmField)
        passwordConfirmLayout = view.findViewById(R.id.passwordConfirmLayout)
        signUpButton = view.findViewById(R.id.signUpButton)
    }

    private fun initListeners() {
        usernameLayout.editText?.addTextChangedListener(
            onTextChanged = { _, _, _, _ ->
                viewModel.validateUsername(usernameField.text.toString())
            }
        )

        fNameLayout.editText?.addTextChangedListener(
            onTextChanged = { _, _, _, _ ->
                viewModel.validateName(fNameField.text.toString())
            }
        )

        phoneLayout.editText?.addTextChangedListener(
            onTextChanged = { _, _, _, _ ->
                viewModel.validatePhone(phoneField.text.toString())
            }
        )

        emailLayout.editText?.addTextChangedListener(
            onTextChanged = { _, _, _, _ ->
                viewModel.validateEmail(emailField.text.toString())
            }
        )

        passwordLayout.editText?.addTextChangedListener(
            onTextChanged = { _, _, _, _ ->
                viewModel.validatePassword(passwordField.text.toString())
            }
        )

        passwordConfirmLayout.editText?.addTextChangedListener(
            onTextChanged = { _, _, _, _ ->
                viewModel.validatePasswordConfirmation(
                    passwordField.text.toString(),
                    passwordConfirmField.text.toString()
                )
            }
        )

        signUpButton.setOnClickListener {
            processRegistration(
                fNameLayout.isHelperTextEnabled,
                usernameLayout.isHelperTextEnabled,
                phoneLayout.isHelperTextEnabled,
                emailLayout.isHelperTextEnabled,
                passwordLayout.isHelperTextEnabled,
                passwordConfirmLayout.isHelperTextEnabled,
            )
        }
    }

    private fun observeValidations() {
        viewModel.nameMessage.observe(viewLifecycleOwner) { result ->
            fNameLayout.helperText = when (result) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.InValid -> result.message
            }
        }

        viewModel.usernameMessage.observe(viewLifecycleOwner) { result ->
            usernameLayout.helperText = when (result) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.InValid -> result.message
            }
        }

        viewModel.phoneMessage.observe(viewLifecycleOwner) { result ->
            phoneLayout.helperText = when (result) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.InValid -> result.message
            }
        }

        viewModel.emailMessage.observe(viewLifecycleOwner) { result ->
            emailLayout.helperText = when (result) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.InValid -> result.message
            }
        }

        viewModel.passwordMessage.observe(viewLifecycleOwner) { result ->
            passwordLayout.helperText = when (result) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.InValid -> result.message
            }
        }

        viewModel.confirmPasswordMessage.observe(viewLifecycleOwner) { result ->
            passwordConfirmLayout.helperText = when (result) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.InValid -> result.message
            }
        }

        viewModel.registerState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ValidateCredentials.Valid -> {
                    val navController = findNavController()
                    navController.popBackStack()
                }

                is ValidateCredentials.InValid -> {
                    createMaterialAlertDialogBuilderOk(
                        requireContext(),
                        "Sign up Failed",
                        result.message,
                        "Ok"
                    ) {}
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    private fun processRegistration(
        nameValidation: Boolean,
        usernameValidation: Boolean,
        emailValidation: Boolean,
        phoneValidation: Boolean,
        passwordValidation: Boolean,
        confirmPasswordValidation: Boolean
    ) {
        val credentialsStatus = viewModel.validateCredentials(
            nameValidation,
            usernameValidation,
            phoneValidation,
            emailValidation,
            passwordValidation,
            confirmPasswordValidation
        )
        when (credentialsStatus) {
            is ValidateCredentials.Valid -> {
                val name = fNameField.text.toString() + " " + lNameField.text.toString()
                val username = usernameField.text.toString()
                val phone = phoneField.text.toString()
                val email = emailField.text.toString()
                val password = passwordField.text.toString()

                viewModel.registerUser(
                    name,
                    username,
                    phone,
                    email,
                    password
                )
            }

            is ValidateCredentials.InValid -> {
                createMaterialAlertDialogBuilderOk(
                    requireContext(),
                    "Sign up Failed",
                    credentialsStatus.message,
                    "Ok"
                ) {}
            }
        }
    }

}