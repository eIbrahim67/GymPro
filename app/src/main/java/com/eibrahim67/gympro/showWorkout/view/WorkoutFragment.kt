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
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSourceImpl
import com.eibrahim67.gympro.core.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions.createFailureResponse
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.main.viewModel.MainViewModelFactory
import com.eibrahim67.gympro.showWorkout.view.adapters.AdapterRVExercisesWorkout
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FirebaseFirestore

class WorkoutFragment : Fragment() {

    private lateinit var workoutCoachName: TextView
    private lateinit var workoutTitle: TextView
    private lateinit var workoutDescription: TextView
    private lateinit var recyclerviewWorkoutExercises: RecyclerView
    private lateinit var workoutFinish: MaterialCardView
    private lateinit var backBtn: MaterialCardView

    private val adapterRVExercisesWorkout = AdapterRVExercisesWorkout { id -> goToExercise(id) }

    private val sharedViewModel: MainViewModel by activityViewModels {
        val remoteRepository =
            RemoteRepositoryImpl(RemoteDataSourceImpl(FirebaseFirestore.getInstance()))
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        MainViewModelFactory(userRepository, remoteRepository)
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
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    sharedViewModel.getCoachById(workout.data?.coachId ?: 0)
                    workoutTitle.text = workout.data?.name
                    workoutDescription.text = workout.data?.description
                    sharedViewModel.getExerciseByIds(workout.data?.exerciseIds ?: emptyList())
                }

                is ResponseEI.Failure -> {
                    createFailureResponse(ResponseEI.Failure(workout.reason), requireContext())
                }
            }
        }
        sharedViewModel.coachById.observe(viewLifecycleOwner) { coach ->
            when (coach) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    workoutCoachName.text = "by ${coach.data?.name}"
                }

                is ResponseEI.Failure -> {
                    createFailureResponse(ResponseEI.Failure(coach.reason), requireContext())
                }
            }
        }
        sharedViewModel.exercisesByIds.observe(viewLifecycleOwner) { exercises ->
            when (exercises) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {

                    adapterRVExercisesWorkout.submitList(exercises.data ?: emptyList())
                }

                is ResponseEI.Failure -> {
                    createFailureResponse(ResponseEI.Failure(exercises.reason), requireContext())
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