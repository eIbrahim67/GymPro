package com.eibrahim67.gympro.ui.myWorkouts

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
import com.eibrahim67.gympro.databinding.FragmentMyWorkoutsBinding
import com.eibrahim67.gympro.utils.response.ResponseEI

class MyWorkoutsFragment : Fragment() {

    private var _binding: FragmentMyWorkoutsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyWorkoutsViewModel by viewModels {
        val app = requireActivity().application as App
        MyWorkoutsViewModelFactory(app.remoteRepository, app.userRepository)
    }

    private val sharedViewModel: MainViewModel by activityViewModels {
        val app = requireActivity().application as App
        MainViewModelFactory(app.userRepository, app.remoteRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyWorkoutsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addNewWorkout.setOnClickListener {
            sharedViewModel.navigateRightTo(R.id.action_createWorkoutFragment)
        }
        val adapterMyWorkouts =
            AdapterMyWorkouts({ id -> goToWorkout(id) }, { id -> deleteWorkout(id) })
        binding.recyclerviewWorkouts.adapter = adapterMyWorkouts

        viewModel.getLoggedInUser()
        viewModel.loggedInUser.observe(viewLifecycleOwner) { user ->

            when (user) {
                is ResponseEI.Loading -> {

                }

                is ResponseEI.Success -> {

                    user.data?.let { viewModel.getMyWorkoutsIds(it.id) }

                }

                is ResponseEI.Failure -> {

                }
            }

        }

        viewModel.myWorkoutsIds.observe(viewLifecycleOwner) { myTrainPlansIds ->

            when (myTrainPlansIds) {
                is ResponseEI.Loading -> {
                }

                is ResponseEI.Success -> {


                    myTrainPlansIds.data?.let {
                        viewModel.getWorkouts(it)
                    }

                }

                is ResponseEI.Failure -> {}
            }

        }

        viewModel.workouts.observe(viewLifecycleOwner) { workouts ->

            when (workouts) {
                is ResponseEI.Loading -> {
                }

                is ResponseEI.Success -> {
                    workouts.data?.let { adapterMyWorkouts.submitList(workouts.data) }

                }

                is ResponseEI.Failure -> {
                }
            }

        }


    }

    private fun goToWorkout(id: Int) {
        sharedViewModel.setTrainPlanId(id)
        sharedViewModel.navigateRightTo(R.id.action_showTrainPlan)
    }

    private fun deleteWorkout(id: Int) {
        viewModel.deleteWorkout(id)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}