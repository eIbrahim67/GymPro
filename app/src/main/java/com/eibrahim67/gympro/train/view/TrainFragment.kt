package com.eibrahim67.gympro.train.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions
import com.eibrahim67.gympro.core.utils.UtilsFunctions.createFailureResponse
import com.eibrahim67.gympro.databinding.FragmentTrainBinding
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.train.view.adapters.AdapterRVWorkouts
import com.eibrahim67.gympro.train.viewModel.TrainViewModel
import com.eibrahim67.gympro.train.viewModel.TrainViewModelFactory

class TrainFragment : Fragment() {

    private var _binding: FragmentTrainBinding? = null
    private val binding get() = _binding!!

    private val adapterRVWorkouts = AdapterRVWorkouts { id -> gotoWorkout(id) }
    private val utils = UtilsFunctions

    private val viewModel: TrainViewModel by viewModels {
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        TrainViewModelFactory(userRepository)
    }

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
        _binding = FragmentTrainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
    }

    private fun initUi() {
        binding.recyclerviewMyTrainPlanWorkouts.adapter = adapterRVWorkouts
        binding.trainBackBtn?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initObservers() {
        viewModel.isUserHaveTrainer()

        viewModel.userHaveTrainer.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    if (response.data) {
                        viewModel.getMyTrainPlan()
                    } else {
                        binding.cardViewGetYourTrainer.visibility = View.VISIBLE
                    }
                }
                is ResponseEI.Failure -> {
                    utils.createFailureResponse(response, requireContext())
                }
            }
        }

        viewModel.myTrainPlan.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Loading -> {
                    Log.e("test", "test")

                }
                is ResponseEI.Success -> {
                    Log.e("test", response.data.toString())
                    binding.trainTitle.visibility = View.VISIBLE
                    binding.recyclerviewMyTrainPlanWorkouts.visibility = View.VISIBLE
                    binding.trainTargetedMuscles.visibility = View.VISIBLE
                    binding.trainTargetedMusclesText.visibility = View.VISIBLE
                    binding.trainDescription.visibility = View.VISIBLE

                    binding.trainTitle.text = response.data?.name
                    binding.trainDescription.text = response.data?.description

                    response.data?.let {
                        sharedViewModel.getTargetedMusclesByIds(it.targetedMuscleIds)
                        sharedViewModel.getWorkoutsByIds(it.workoutsIds)
                    }
                }
                is ResponseEI.Failure -> {
                    utils.createFailureResponse(response, requireContext())
                }
            }
        }

        sharedViewModel.targetedMusclesByIds.observe(viewLifecycleOwner) { muscles ->
            when (muscles) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    binding.trainTargetedMusclesText.text = muscles.data
                }
                is ResponseEI.Failure -> {
                    createFailureResponse(ResponseEI.Failure(muscles.reason), requireContext())
                }
            }
        }

        sharedViewModel.workoutsByIds.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    response.data?.let { adapterRVWorkouts.submitList(it) }
                }
                is ResponseEI.Failure -> {
                    utils.createFailureResponse(response, requireContext())
                }
            }
        }
    }

    private fun gotoWorkout(id: Int) {
        sharedViewModel.setWorkoutId(id)
        sharedViewModel.navigateRightTo(com.eibrahim67.gympro.R.id.action_workout)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
