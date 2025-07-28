package com.eibrahim67.gympro.showWorkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.utils.UtilsFunctions.createFailureResponse
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.showWorkout.view.adapters.AdapterRVExercisesWorkout
import com.eibrahim67.gympro.train.viewModel.TrainViewModelFactory
import com.google.android.material.card.MaterialCardView

class WorkoutFragment : Fragment() {

    private lateinit var workoutCoachName: TextView
    private lateinit var workoutTitle: TextView
    private lateinit var workoutDescription: TextView
    private lateinit var recyclerviewWorkoutExercises: RecyclerView
    private lateinit var workoutFinish: MaterialCardView
    private lateinit var backBtn: MaterialCardView

    private val adapterRVExercisesWorkout = AdapterRVExercisesWorkout { id -> goToExercise(id) }

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
        initListeners()
    }

    private fun initListeners() {
        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initObservers() {
        sharedViewModel.workoutId.observe(viewLifecycleOwner) { id ->
            sharedViewModel.getWorkoutById(id)
        }
        sharedViewModel.workoutById.observe(viewLifecycleOwner) { workout ->
            when (workout) {
                is Response.Loading -> {}
                is Response.Success -> {
                    sharedViewModel.getCoachById(workout.data?.coachId ?: 0)
                    workoutTitle.text = workout.data?.name
                    workoutDescription.text = workout.data?.description
                    sharedViewModel.getExerciseByIds(workout.data?.exerciseIds ?: emptyList())
                }

                is Response.Failure -> {
                    createFailureResponse(Response.Failure(workout.reason), requireContext())
                }
            }
        }
        sharedViewModel.coachById.observe(viewLifecycleOwner) { coach ->
            when (coach) {
                is Response.Loading -> {}
                is Response.Success -> {
                    workoutCoachName.text = "by ${coach.data?.name}"
                }

                is Response.Failure -> {
                    createFailureResponse(Response.Failure(coach.reason), requireContext())
                }
            }
        }
        sharedViewModel.exercisesByIds.observe(viewLifecycleOwner) { exercises ->
            when (exercises) {
                is Response.Loading -> {}
                is Response.Success -> {
                    adapterRVExercisesWorkout.submitList(exercises.data ?: emptyList())
                }

                is Response.Failure -> {
                    createFailureResponse(Response.Failure(exercises.reason), requireContext())
                }
            }
        }
    }

    private fun initUi(view: View) {
        workoutCoachName = view.findViewById(R.id.workoutCoachName)
        workoutTitle = view.findViewById(R.id.workoutTitle)
        workoutDescription = view.findViewById(R.id.workoutDescription)
        workoutFinish = view.findViewById(R.id.workoutFinish)
        recyclerviewWorkoutExercises = view.findViewById(R.id.recyclerviewWorkoutExercises)
        recyclerviewWorkoutExercises.adapter = adapterRVExercisesWorkout
        backBtn = view.findViewById(R.id.workoutBackBtn)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_workout, container, false)
    }

    private fun goToExercise(id: Int) {
        sharedViewModel.setExerciseId(id)
        sharedViewModel.navigateRightTo(R.id.action_exercise)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.navigateRightTo(null)
    }
}