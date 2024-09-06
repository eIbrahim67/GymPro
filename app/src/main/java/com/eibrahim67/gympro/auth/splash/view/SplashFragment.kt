package com.eibrahim67.gympro.auth.splash.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.auth.splash.viewModel.SplashViewModel
import com.eibrahim67.gympro.auth.splash.viewModel.SplashViewModelFactory
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.utils.UtilsFunctions
import com.eibrahim67.gympro.main.view.activities.MainActivity

class SplashFragment : Fragment() {

    private val utils = UtilsFunctions

    private val viewModel: SplashViewModel by viewModels {
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        SplashViewModelFactory(userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.thereIsLoggedInUser()

        viewModel.thereIsLoggedInUser.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Response.Loading -> {}

                is Response.Success -> {

                    Handler(Looper.getMainLooper()).postDelayed({
                        if (response.data) {
                            startActivity(Intent(requireActivity(), MainActivity::class.java))
                            requireActivity().finish()
                        } else {
                            findNavController().navigate(R.id.action_splashFragment_to_signinFragment)
                        }
                    }, 2000)
                }

                is Response.Failure -> {
                    utils.createFailureResponse(response, requireContext())
                }
            }

        }

    }

}