package com.eibrahim67.gympro.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.databinding.FragmentProfileBinding
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.profile.viewModel.ProfileViewModel
import com.eibrahim67.gympro.train.viewModel.TrainViewModelFactory
import kotlin.getValue

class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by viewModels()

    private val sharedViewModel: MainViewModel by activityViewModels {
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        TrainViewModelFactory(userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
                sharedViewModel.navigateRightTo(R.id.action_becomeTrainerFragment)
            }

            btnMyChats.setOnClickListener {
                sharedViewModel.navigateRightTo(R.id.action_myChatsFragment)
            }

            btnMyClients.setOnClickListener {
                sharedViewModel.navigateRightTo(R.id.action_myClientsFragment)
            }

            btnMyPlans.setOnClickListener {
                sharedViewModel.navigateRightTo(R.id.action_myTrainingPlansFragment)
            }

            btnMyProgress.setOnClickListener {
                sharedViewModel.navigateRightTo(R.id.action_myProgressFragment)
            }

            btnMostImprovedMuscles.setOnClickListener {
                sharedViewModel.navigateRightTo(R.id.action_mostImprovedMusclesFragment)
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
                performLogout() // Replace with actual logout logic
            }

            backBtn.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun performLogout(){}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}