package com.eibrahim67.gympro.ui.showTrainPlan.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.App
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.main.viewModel.MainViewModel
import com.eibrahim67.gympro.core.main.viewModel.MainViewModelFactory
import com.eibrahim67.gympro.databinding.FragmentShowTrainPlanBinding
import com.eibrahim67.gympro.ui.showTrainPlan.view.adapters.AdapterRVWorkoutsTrainingPlan
import com.eibrahim67.gympro.ui.showTrainPlan.viewModel.ShowTrainPlanViewModel
import com.eibrahim67.gympro.ui.showTrainPlan.viewModel.ShowTrainPlanViewModelFactory
import com.eibrahim67.gympro.utils.UtilsFunctions.createFailureResponse
import com.eibrahim67.gympro.utils.UtilsFunctions.createMaterialAlertDialogBuilderOkCancel
import com.eibrahim67.gympro.utils.response.ResponseEI

class ShowTrainPlanFragment : Fragment() {


    private var _binding: FragmentShowTrainPlanBinding? = null
    private val binding get() = _binding!!

    private val adapterRVWorkouts = AdapterRVWorkoutsTrainingPlan { id -> gotoWorkout(id) }

    private val viewModel: ShowTrainPlanViewModel by viewModels {
        val app = requireActivity().application as App
        ShowTrainPlanViewModelFactory(app.remoteRepository)
    }

    private val sharedViewModel: MainViewModel by activityViewModels {
        val app = requireActivity().application as App
        MainViewModelFactory(app.userRepository, app.remoteRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowTrainPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initObservers()
        initListeners()
    }

    private fun initListeners() {
        binding.trainPlanSetAsDefaultBtn.setOnClickListener {
            createMaterialAlertDialogBuilderOkCancel(
                requireContext(),
                "Change train plan",
                "You will use this train plan as default?",
                "OK",
                "Cancel"
            ) {
                sharedViewModel.updateMyTrainPlan()
                sharedViewModel.updateMyCoachState(true)
            }
        }

        // Common back button handler
        binding.trainPlanBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initUI() {
        binding.recyclerviewFeaturePlans.adapter = adapterRVWorkouts
    }

    private fun initObservers() {
        sharedViewModel.trainPlanId.observe(viewLifecycleOwner) { id ->
            viewModel.getTrainingPlanById(id)
        }

        viewModel.trainPlan.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResponseEI.Loading -> Unit
                is ResponseEI.Success -> result.data?.let { plan ->
                    Glide.with(requireContext()).load(plan.imageUrl).into(binding.trainPlanImage)
                    binding.trainPlanTitle.text = plan.name
                    binding.trainPlanDescription.text = plan.description
                    binding.trainPlanDifficulty.text = plan.difficultyLevel
                    binding.trainPlanAvgTime.text = "${plan.avgTimeMinPerWorkout} min"
                    binding.trainPlanDaysPerTrainingWeek.text =
                        "${plan.durationDaysPerTrainingWeek} Days"
                    sharedViewModel.getCoachById(plan.coachId ?: "")
                    sharedViewModel.getMusclesByIds(plan.targetedMuscleIds ?: emptyList())
                    plan.workoutsIds?.let { sharedViewModel.getWorkoutsByIds(it) }
                }

                is ResponseEI.Failure -> createFailureResponse(result, requireContext())
            }
        }

        sharedViewModel.coachById.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResponseEI.Loading -> Unit
                is ResponseEI.Success -> binding.trainPlanCoachName.text =
                    "by ${result.data?.name.orEmpty()}"

                is ResponseEI.Failure -> createFailureResponse(result, requireContext())
            }
        }

        sharedViewModel.musclesByIds.observe(viewLifecycleOwner) { muscles ->
            when (muscles) {
                is ResponseEI.Loading -> Unit
                is ResponseEI.Success -> {
                    var targetedMusclesText = ""
                    muscles.data?.map {
                        targetedMusclesText += it?.name ?: "Unknown"
                    }
                    binding.targetedMusclesText.text = targetedMusclesText

                }

                is ResponseEI.Failure -> createFailureResponse(muscles, requireContext())
            }
        }

        sharedViewModel.workoutsByIds.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResponseEI.Loading -> Unit
                is ResponseEI.Success -> adapterRVWorkouts.submitList(result.data.orEmpty())
                is ResponseEI.Failure -> createFailureResponse(result, requireContext())
            }
        }
    }


    private fun gotoWorkout(id: Int) {
        sharedViewModel.setWorkoutId(id)
        sharedViewModel.navigateRightTo(R.id.action_workout)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        sharedViewModel.navigateRightTo(null)
    }
}