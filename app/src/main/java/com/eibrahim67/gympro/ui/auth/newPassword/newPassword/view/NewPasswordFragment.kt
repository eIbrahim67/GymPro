package com.eibrahim67.gympro.ui.auth.newPassword.newPassword.view

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
import com.eibrahim67.gympro.App
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.utils.response.ResponseEI
import com.eibrahim67.gympro.utils.UtilsFunctions
import com.eibrahim67.gympro.utils.UtilsFunctions.createMaterialAlertDialogBuilderOk
import com.eibrahim67.gympro.ui.auth.authActivity.viewModel.AuthViewModel
import com.eibrahim67.gympro.ui.auth.newPassword.newPassword.viewModel.NewPasswordViewModel
import com.eibrahim67.gympro.ui.auth.newPassword.newPassword.viewModel.NewPasswordViewModelFactory
import com.eibrahim67.gympro.ui.auth.signup.model.ValidateCredentials
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class NewPasswordFragment : Fragment() {

    private lateinit var newPasswordEditText: TextInputEditText
    private lateinit var confirmPasswordEditText: TextInputEditText
    private lateinit var newPasswordInputLayout: TextInputLayout
    private lateinit var confirmPasswordInputLayout: TextInputLayout
    private lateinit var resetPasswordButton: Button
    private lateinit var backToLoginTv: TextView
    private var navController: NavController? = null

    private val utils = UtilsFunctions

    private val sharedViewModel: AuthViewModel by activityViewModels()
    private val viewModel: NewPasswordViewModel by viewModels {
        val app = requireActivity().application as App
        NewPasswordViewModelFactory(app.userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_new_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi(view)

        listenerActions()

        observers()

    }

    private fun observers() {
        viewModel.password.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Loading -> {
                    //TODO:show progress bar
                }

                is ResponseEI.Success -> {
                    backToLoginTv.callOnClick()
                }

                is ResponseEI.Failure -> (utils.createFailureResponse(
                    ResponseEI.Failure(response.reason), requireContext()
                ))
            }

        }

        viewModel.confirmPasswordMessage.observe(viewLifecycleOwner) { result ->
            confirmPasswordInputLayout.helperText = when (result) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.InValid -> result.message
            }
        }

        viewModel.passwordMessage.observe(viewLifecycleOwner) { result ->
            newPasswordInputLayout.helperText = when (result) {
                is ValidateCredentials.Valid -> null
                is ValidateCredentials.InValid -> result.message
            }
        }
    }

    private fun listenerActions() {
        newPasswordInputLayout.editText?.addTextChangedListener(
            onTextChanged = { _, _, _, _ ->
                viewModel.validatePassword(newPasswordEditText.text.toString())
            })

        confirmPasswordInputLayout.editText?.addTextChangedListener(
            onTextChanged = { _, _, _, _ ->
                viewModel.validatePasswordConfirmation(
                    newPasswordEditText.text.toString(), confirmPasswordEditText.text.toString()
                )
            })

        resetPasswordButton.setOnClickListener {

            val result = viewModel.validateCredentials(
                newPasswordInputLayout.isHelperTextEnabled,
                confirmPasswordInputLayout.isHelperTextEnabled
            )

            when (result) {
                is ValidateCredentials.Valid -> {
                    viewModel.updatePassword(
                        sharedViewModel.email.value.toString(), newPasswordEditText.text.toString()
                    )
                }

                is ValidateCredentials.InValid -> {
                    createMaterialAlertDialogBuilderOk(
                        requireContext(), "Reset password Failed", result.message, "Ok"
                    ) {}
                }
            }

        }

        backToLoginTv.setOnClickListener {
            navController?.popBackStack(R.id.signinFragment, false)
        }
    }

    private fun initUi(view: View) {
        newPasswordEditText = view.findViewById(R.id.newPasswordEditText)
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText)
        confirmPasswordInputLayout = view.findViewById(R.id.confirmPasswordInputLayout)
        newPasswordInputLayout = view.findViewById(R.id.newPasswordInputLayout)
        resetPasswordButton = view.findViewById(R.id.resetPasswordButton)
        backToLoginTv = view.findViewById(R.id.backToLoginTvNewPassword)


        navController = requireView().findNavController()
    }


}
