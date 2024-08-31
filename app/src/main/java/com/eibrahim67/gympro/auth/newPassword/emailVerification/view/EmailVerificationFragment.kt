package com.eibrahim67.gympro.auth.newPassword.emailVerification.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.auth.authActivity.viewModel.AuthViewModel
import com.eibrahim67.gympro.auth.newPassword.emailVerification.viewModel.EmailVerificationViewModel
import com.eibrahim67.gympro.auth.signup.model.ValidateCredentials
import com.eibrahim67.gympro.core.utils.UtilsFunctions.createMaterialAlertDialogBuilderOk
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EmailVerificationFragment : Fragment() {

    private lateinit var emailEditText: TextInputEditText
    private lateinit var buttonResetPassword: Button
    private lateinit var buttonBackToLogin: TextView
    private lateinit var emailInputLayout: TextInputLayout

    private val viewModel: EmailVerificationViewModel by viewModels()
    private val sharedViewModel: AuthViewModel by activityViewModels()


    private var navController: NavController? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_email_verification, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi(view)

        buttonResetPassword.setOnClickListener {
            when (val state = viewModel.validateCredentials(emailInputLayout.isHelperTextEnabled)) {
                is ValidateCredentials.Valid -> {

                    sharedViewModel.setDate(emailEditText.text.toString())

                    navController?.navigate(
                        R.id.action_emailVerificationFragment_to_checkEmailFragment,
                    )
                }

                is ValidateCredentials.InValid -> {
                    createMaterialAlertDialogBuilderOk(
                        requireContext(),
                        "Email verification Failed",
                        state.message,
                        "Ok"
                    ) {}
                }
            }

        }

        buttonBackToLogin.setOnClickListener {
            navController?.popBackStack(R.id.signinFragment, false)
        }

        emailInputLayout.editText?.addTextChangedListener(
            onTextChanged = { _, _, _, _ ->
                viewModel.validateEmail(emailEditText.text.toString())
            }
        )

        viewModel.emailMessage.observe(viewLifecycleOwner) { result ->
            emailInputLayout.helperText = when (result) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.InValid -> result.message
            }
        }

    }

    private fun initUi(view: View) {

        emailEditText = view.findViewById(R.id.verificationEmailEditText)
        emailInputLayout = view.findViewById(R.id.verificationEmailInputLayout)
        buttonResetPassword = view.findViewById(R.id.buttonResetPassword)
        buttonBackToLogin = view.findViewById(R.id.buttonBackToLoginVerification)

        navController = requireView().findNavController()
    }

    companion object {

        const val EMAIL_KEY = "email_key_67"

    }

}