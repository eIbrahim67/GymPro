package com.eibrahim67.gympro.profile.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.auth.authActivity.view.AuthActivity
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSourceImpl
import com.eibrahim67.gympro.core.response.ResponseEI
import com.eibrahim67.gympro.databinding.FragmentProfileBinding
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.main.viewModel.MainViewModelFactory
import com.eibrahim67.gympro.profile.viewModel.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private val sharedViewModel: MainViewModel by activityViewModels {
        val remoteRepository =
            RemoteRepositoryImpl(RemoteDataSourceImpl(FirebaseFirestore.getInstance()))
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        MainViewModelFactory(userRepository, remoteRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnPersonalData.setOnClickListener {
                sharedViewModel.navigateRightTo(R.id.action_personalDataFragment)
            }

            btnBeComeATrainer.setOnClickListener {
//                sharedViewModel.navigateRightTo(R.id.action_becomeTrainerFragment)
            }

            btnMyChats.setOnClickListener {
//                sharedViewModel.navigateRightTo(R.id.action_myChatsFragment)
            }

            btnMyClients.setOnClickListener {
//                sharedViewModel.navigateRightTo(R.id.action_myClientsFragment)
            }

            btnMyPlans.setOnClickListener {
                sharedViewModel.navigateRightTo(R.id.action_myTrainingPlansFragment)
            }

            btnMyProgress.setOnClickListener {
//                sharedViewModel.navigateRightTo(R.id.action_myProgressFragment)
            }

            btnMostImprovedMuscles.setOnClickListener {
//                sharedViewModel.navigateRightTo(R.id.action_mostImprovedMusclesFragment)
            }

            btnSecurityPolicies.setOnClickListener {
                sharedViewModel.navigateRightTo(R.id.action_securityPoliciesFragment)
            }

            btnHelpFeedback.setOnClickListener {
                sharedViewModel.navigateRightTo(R.id.action_helpFeedbackFragment)
            }

            btnAbout.setOnClickListener {
                sharedViewModel.navigateRightTo(R.id.action_aboutFragment)
            }

            btnLogOut.setOnClickListener {
                sharedViewModel.logout()
            }

            backBtn.setOnClickListener {
                findNavController().navigateUp()
            }

        }
        sharedViewModel.logout.observe(viewLifecycleOwner) { state ->

            sharedViewModel.exerciseById.observe(viewLifecycleOwner) { exercise ->
                when (state) {
                    is ResponseEI.Loading -> {}
                    is ResponseEI.Success -> {
                        startActivity(Intent(requireContext(), AuthActivity::class.java))
                        requireActivity().finish()
                    }

                    is ResponseEI.Failure -> {
                        Snackbar.make(requireView(), "Something Wrong!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Try Again?") {
                                sharedViewModel.logout()

                            }.show()
                    }
                }
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}