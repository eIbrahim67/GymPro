package com.eibrahim67.gympro.ui.myExercises

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
import com.eibrahim67.gympro.databinding.FragmentMyExercisesBinding
import com.eibrahim67.gympro.utils.response.ResponseEI
import com.google.firebase.auth.FirebaseAuth

class MyExercisesFragment : Fragment() {

    private var _binding: FragmentMyExercisesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyExercisesViewModel by viewModels {
        val app = requireActivity().application as App
        MyExercisesViewModelFactory(app.remoteRepository, app.userRepository)
    }

    private val sharedViewModel: MainViewModel by activityViewModels {
        val app = requireActivity().application as App
        MainViewModelFactory(app.userRepository, app.remoteRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyExercisesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addNewExercise.setOnClickListener {
            sharedViewModel.navigateRightTo(R.id.action_createExerciseFragment)
        }
        val adapterMyExercises =
            AdapterMyExercises({ id -> goToExercise(id) }, { id -> deleteExercise(id) })
        binding.recyclerviewExercises.adapter = adapterMyExercises

        viewModel.getLoggedInUser()
        viewModel.loggedInUser.observe(viewLifecycleOwner) { user ->

            when (user) {
                is ResponseEI.Loading -> {

                }

                is ResponseEI.Success -> {

                    user.data?.let { viewModel.getMyExercisesIds(FirebaseAuth.getInstance().currentUser?.uid.toString()) }

                }

                is ResponseEI.Failure -> {

                }
            }

        }

        viewModel.myExercisesIds.observe(viewLifecycleOwner) { myTrainPlansIds ->

            when (myTrainPlansIds) {
                is ResponseEI.Loading -> {
                }

                is ResponseEI.Success -> {


                    myTrainPlansIds.data?.let {
                        viewModel.getExercises(it)
                    }

                }

                is ResponseEI.Failure -> {}
            }

        }

        viewModel.exercises.observe(viewLifecycleOwner) { exercises ->

            when (exercises) {
                is ResponseEI.Loading -> {
                }

                is ResponseEI.Success -> {
                    exercises.data?.let { adapterMyExercises.submitList(exercises.data) }

                }

                is ResponseEI.Failure -> {
                }
            }

        }


    }

    private fun goToExercise(id: Int) {
        sharedViewModel.setTrainPlanId(id)
        sharedViewModel.navigateRightTo(R.id.action_showTrainPlan)
    }

    private fun deleteExercise(id: Int) {
        viewModel.deleteExercise(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}