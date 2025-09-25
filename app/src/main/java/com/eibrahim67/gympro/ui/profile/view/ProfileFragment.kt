package com.eibrahim67.gympro.ui.profile.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.App
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.main.viewModel.MainViewModel
import com.eibrahim67.gympro.core.main.viewModel.MainViewModelFactory
import com.eibrahim67.gympro.databinding.FragmentProfileBinding
import com.eibrahim67.gympro.ui.auth.authActivity.view.AuthActivity
import com.eibrahim67.gympro.ui.profile.viewModel.ProfileViewModel
import com.eibrahim67.gympro.utils.response.ResponseEI
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private val sharedViewModel: MainViewModel by activityViewModels {
        val app = requireActivity().application as App
        MainViewModelFactory(app.userRepository, app.remoteRepository)
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
                sharedViewModel.navigateRightTo(R.id.action_profileFragment_to_personalDataFragment)
            }

            btnBeComeATrainer.setOnClickListener {
                sharedViewModel.navigateRightTo(R.id.action_becomeTrainerFragment)
            }

            btnMyChats.setOnClickListener {
                sharedViewModel.navigateRightTo(R.id.action_myChatsFragment)
            }

//            btnMyClients.setOnClickListener {
//                sharedViewModel.navigateRightTo(R.id.action_myClientsFragment)
//            }

            btnMyPlans.setOnClickListener {
                sharedViewModel.navigateRightTo(R.id.action_myTrainingPlansFragment)
            }

            btnMyWorkouts.setOnClickListener {
                sharedViewModel.navigateRightTo(R.id.action_myWorkoutsFragment)
            }

            btnMyExercises.setOnClickListener {
                sharedViewModel.navigateRightTo(R.id.action_myExercisesFragment)
            }

//            btnMyProgress.setOnClickListener {
//                sharedViewModel.navigateRightTo(R.id.action_myProgressFragment)
//            }

//            btnMostImprovedMuscles.setOnClickListener {
//                sharedViewModel.navigateRightTo(R.id.action_mostImprovedMusclesFragment)
//            }

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

                AlertDialog.Builder(requireContext()).setTitle("Log Out")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Log Out") { _, _ ->
                        sharedViewModel.logout()
                    }.setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }.show()

            }

            backBtn.setOnClickListener {
                findNavController().navigateUp()
            }

        }
        sharedViewModel.logout.observe(viewLifecycleOwner) { state ->

            when (state) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    FirebaseAuth.getInstance().signOut()
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}