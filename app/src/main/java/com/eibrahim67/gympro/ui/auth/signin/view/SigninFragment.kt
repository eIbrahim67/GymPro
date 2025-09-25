package com.eibrahim67.gympro.ui.auth.signin.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.eibrahim67.gympro.App
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.utils.UtilsFunctions.createMaterialAlertDialogBuilderOk
import com.eibrahim67.gympro.core.main.view.activities.MainActivity
import com.eibrahim67.gympro.ui.auth.signin.viewModel.SigninViewModel
import com.eibrahim67.gympro.ui.auth.signin.viewModel.SigninViewModelFactory
import com.eibrahim67.gympro.ui.auth.signup.model.ValidateCredentials
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SigninFragment : Fragment() {

    private lateinit var emailField: TextInputEditText
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordField: TextInputEditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var navController: NavController
    private lateinit var signInButton: MaterialCardView
    private lateinit var signUpText: TextView
    private lateinit var forgetPasswordText: TextView

    private val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    private val viewModel: SigninViewModel by viewModels {
        val app = requireActivity().application as App
        SigninViewModelFactory(app.userRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initListeners()
        initObservers()
    }

    private fun initViews(view: View) {
        emailField = view.findViewById(R.id.emailField2)
        emailLayout = view.findViewById(R.id.emailLayout2)
        passwordField = view.findViewById(R.id.passwordField2)
        passwordLayout = view.findViewById(R.id.passwordLayout2)
        signInButton = view.findViewById(R.id.signInButton)
        signUpText = view.findViewById(R.id.signUpText)
        forgetPasswordText = view.findViewById(R.id.forgetPasswordText)

        navController = requireView().findNavController()
    }

    private fun initListeners() {
        signInButton.setOnClickListener {
            processLogin()
        }

        signUpText.setOnClickListener {
            navController.navigate(
                R.id.action_signinFragment_to_signupFragment,
                null,
                navOptions
            )
        }

        forgetPasswordText.setOnClickListener {
            navController.navigate(
                R.id.action_signinFragment_to_emailVerificationFragment,
                null,
                navOptions
            )
        }

    }

    private fun initObservers() {
        viewModel.isUserValid.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ValidateCredentials.Valid -> {
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }

                is ValidateCredentials.InValid -> createMaterialAlertDialogBuilderOk(
                    requireContext(),
                    "Sign in Failed",
                    result.message,
                    "Ok"
                ) {}

                else -> {}
            }
        }
    }

    private fun processLogin() {
        val email = emailField.text.toString()
        val password = passwordField.text.toString()
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        if (email.isBlank() || password.isBlank() || trimmedPassword != password || trimmedEmail != email) {
            createMaterialAlertDialogBuilderOk(
                requireContext(),
                "Sign in Failed",
                "Please fill in all fields or remove spaces",
                "Ok"
            ) {}
            return
        } else {
//            viewModel.checkUser(email, password)
            viewModel.loginUser(email, password) { result ->
                if (result)
                    Log.i("loginUserFirebase", "True")
                else
                    Log.i("loginUserFirebase", "False")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }
}