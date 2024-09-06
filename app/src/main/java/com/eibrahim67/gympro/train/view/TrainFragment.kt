package com.eibrahim67.gympro.train.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.utils.UtilsFunctions
import com.eibrahim67.gympro.core.utils.UtilsFunctions.createFailureResponse
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.train.view.adapters.AdapterRVWorkouts
import com.eibrahim67.gympro.train.viewModel.TrainViewModel
import com.eibrahim67.gympro.train.viewModel.TrainViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView

class TrainFragment : Fragment() {

    private lateinit var stateActiveLineTrain: View
    private lateinit var trainTitle: TextView
    private lateinit var trainTargetedMuscles: TextView
    private lateinit var trainTargetedMusclesText: TextView
    private lateinit var recyclerviewWorkoutsExercises: RecyclerView
    private lateinit var cardViewGetYourTrainer: MaterialCardView
    private lateinit var bottomNavigationView: BottomNavigationView

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
        initObservers()
    }

    private fun initUi(view: View) {
        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.VISIBLE
        trainTitle = view.findViewById(R.id.trainTitle)
        stateActiveLineTrain = view.findViewById(R.id.stateActiveLineTrain)
        cardViewGetYourTrainer = view.findViewById(R.id.cardViewGetYourTrainer)
        trainTargetedMuscles = view.findViewById(R.id.trainTargetedMuscles)
        trainTargetedMusclesText = view.findViewById(R.id.trainTargetedMusclesText)
        recyclerviewWorkoutsExercises = view.findViewById(R.id.recyclerviewMyTrainPlanWorkouts)
        recyclerviewWorkoutsExercises.adapter = adapterRVWorkouts
    }

    private fun initObservers() {
        viewModel.isUserHaveTrainer()

        viewModel.userHaveTrainer.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {}
                is Response.Success -> {
                    if (response.data) {
                        sharedViewModel.getMyTrainPlan()
                    } else {
                        cardViewGetYourTrainer.visibility = View.VISIBLE
                    }
                }

                is Response.Failure -> {
                    utils.createFailureResponse(response, requireContext())
                }
            }
        }
        sharedViewModel.myTrainPlan.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {}
                is Response.Success -> {
                    trainTitle.visibility = View.VISIBLE
                    recyclerviewWorkoutsExercises.visibility = View.VISIBLE
                    stateActiveLineTrain.visibility = View.VISIBLE
                    trainTargetedMuscles.visibility = View.VISIBLE
                    trainTargetedMusclesText.visibility = View.VISIBLE
                    stateActiveLineTrain.visibility = View.VISIBLE
                    trainTitle.text = response.data?.name
                    response.data?.let { sharedViewModel.getTargetedMusclesByIds(it.targetedMuscleIds) }
                    response.data?.let { sharedViewModel.getWorkoutsByIds(it.workoutsIds) }
                }

                is Response.Failure -> {
                    utils.createFailureResponse(response, requireContext())
                }
            }
        }
        sharedViewModel.targetedMusclesByIds.observe(viewLifecycleOwner) { muscles ->
            when (muscles) {
                is Response.Loading -> {}
                is Response.Success -> {
                    trainTargetedMusclesText.text = muscles.data
                }

                is Response.Failure -> {
                    createFailureResponse(Response.Failure(muscles.reason), requireContext())
                }
            }
        }
        sharedViewModel.workoutsByIds.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {}
                is Response.Success -> {
                    response.data?.let { adapterRVWorkouts.submitList(it) }
                }

                is Response.Failure -> {
                    utils.createFailureResponse(response, requireContext())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_train, container, false)
    }

    private fun gotoWorkout(id: Int) {
        sharedViewModel.setWorkoutId(id)
        sharedViewModel.navigateTo(R.id.action_workout)
    }
}