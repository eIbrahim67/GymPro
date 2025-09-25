package com.eibrahim67.gympro.ui.auth.signup.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.App
import com.eibrahim67.gympro.utils.UtilsFunctions.createMaterialAlertDialogBuilderOk
import com.eibrahim67.gympro.databinding.FragmentSignupBinding
import com.eibrahim67.gympro.ui.auth.signup.model.ValidateCredentials
import com.eibrahim67.gympro.ui.auth.signup.viewModel.SignupViewModel
import com.eibrahim67.gympro.ui.auth.signup.viewModel.SignupViewModelFactory

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignupViewModel by viewModels {
        val app = requireActivity().application as App
        SignupViewModelFactory(app.userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        observeValidations()
    }

    private fun initListeners() {
        binding.usernameLayout.editText?.addTextChangedListener {
            viewModel.validateUsername(binding.usernameField.text?.toString().orEmpty())
        }

        binding.fNameLayout.editText?.addTextChangedListener {
            viewModel.validateName(binding.fNameField.text?.toString().orEmpty())
        }

        binding.phoneLayout.editText?.addTextChangedListener {
            viewModel.validatePhone(binding.phoneField.text?.toString().orEmpty())
        }

        binding.emailLayout.editText?.addTextChangedListener {
            viewModel.validateEmail(binding.emailField.text?.toString().orEmpty())
        }

        binding.passwordLayout.editText?.addTextChangedListener {
            viewModel.validatePassword(binding.passwordField.text?.toString().orEmpty())
        }

        binding.passwordConfirmLayout.editText?.addTextChangedListener {
            viewModel.validatePasswordConfirmation(
                binding.passwordField.text?.toString().orEmpty(),
                binding.passwordConfirmField.text?.toString().orEmpty()
            )
        }

        binding.signUpButton.setOnClickListener {
            try {
                processRegistration(
                    binding.fNameLayout.isHelperTextEnabled,
                    binding.usernameLayout.isHelperTextEnabled,
                    binding.phoneLayout.isHelperTextEnabled,
                    binding.emailLayout.isHelperTextEnabled,
                    binding.passwordLayout.isHelperTextEnabled,
                    binding.passwordConfirmLayout.isHelperTextEnabled
                )
            } catch (e: Exception) {
                Log.e("SignupFragment", "Error in registration: ${e.message}")
            }
        }
    }

    private fun observeValidations() {
        viewModel.nameMessage.observe(viewLifecycleOwner) { result ->
            binding.fNameLayout.helperText = if (result is ValidateCredentials.InValid) result.message else null
        }

        viewModel.usernameMessage.observe(viewLifecycleOwner) { result ->
            binding.usernameLayout.helperText = if (result is ValidateCredentials.InValid) result.message else null
        }

        viewModel.phoneMessage.observe(viewLifecycleOwner) { result ->
            binding.phoneLayout.helperText = if (result is ValidateCredentials.InValid) result.message else null
        }

        viewModel.emailMessage.observe(viewLifecycleOwner) { result ->
            binding.emailLayout.helperText = if (result is ValidateCredentials.InValid) result.message else null
        }

        viewModel.passwordMessage.observe(viewLifecycleOwner) { result ->
            binding.passwordLayout.helperText = if (result is ValidateCredentials.InValid) result.message else null
        }

        viewModel.confirmPasswordMessage.observe(viewLifecycleOwner) { result ->
            binding.passwordConfirmLayout.helperText = if (result is ValidateCredentials.InValid) result.message else null
        }

        viewModel.registerState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ValidateCredentials.Valid -> {
                    findNavController().popBackStack()
                }
                is ValidateCredentials.InValid -> {
                    createMaterialAlertDialogBuilderOk(
                        requireContext(), "Sign up Failed", result.message, "Ok"
                    ) {}
                }
            }
        }
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
                val name = "${binding.fNameField.text} ${binding.lNameField.text}".trim()
                val username = binding.usernameField.text?.toString().orEmpty()
                val phone = binding.phoneField.text?.toString().orEmpty()
                val email = binding.emailField.text?.toString().orEmpty()
                val password = binding.passwordField.text?.toString().orEmpty()

                viewModel.registerUser(name, username, phone, email, password)

                viewModel.registerUserFirebase(name, email, password) { result ->
                    Log.i("registerUserFirebase", if (result) "True" else "False")
                }
            }
            is ValidateCredentials.InValid -> {
                createMaterialAlertDialogBuilderOk(
                    requireContext(), "Sign up Failed", credentialsStatus.message, "Ok"
                ) {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
