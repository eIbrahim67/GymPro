package com.eibrahim67.gympro.showExercise.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.utils.UtilsFunctions.createFailureResponse
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.showExercise.view.adapters.AdapterRVExercisesResults
import com.eibrahim67.gympro.showExercise.view.bottomSheets.BottomSheetAddNewSet
import com.eibrahim67.gympro.train.viewModel.TrainViewModelFactory
import com.google.android.material.card.MaterialCardView

class ExerciseFragment : Fragment() {

    private lateinit var exerciseVideoPlayer: VideoView
    private lateinit var exerciseTitle: TextView
    private lateinit var exerciseHint: TextView
    private lateinit var exerciseHintFirstSet: TextView
    private lateinit var recyclerviewExerciseSetsDetails: RecyclerView
    private lateinit var exerciseDoneBtn: MaterialCardView
    private lateinit var addNewSetBtn: MaterialCardView

    private val adapterRVExercisesResults = AdapterRVExercisesResults()

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
        addNewSetBtn.setOnClickListener {
            val bottomSheetAddNewSet = BottomSheetAddNewSet()
            bottomSheetAddNewSet.show(
                requireActivity().supportFragmentManager,
                BottomSheetAddNewSet.TAG
            )
        }
    }

    private fun initObservers() {
        sharedViewModel.fetchDateExerciseData()
        sharedViewModel.exerciseId.observe(viewLifecycleOwner) { id ->
            sharedViewModel.getExerciseById(id)
        }
        sharedViewModel.userDataExercise.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {}
                is Response.Success -> {
                    val weights = response.data[sharedViewModel.exerciseId.value]?.keys?.toList()
                    val reps = response.data[sharedViewModel.exerciseId.value]?.values?.toList()
                    adapterRVExercisesResults.submitListWeight(weights)
                    adapterRVExercisesResults.submitListReps(reps)

                }

                is Response.Failure -> {}
            }
        }

        sharedViewModel.exerciseById.observe(viewLifecycleOwner) { exercise ->
            when (exercise) {
                is Response.Loading -> {}
                is Response.Success -> {
                    //exerciseVideoPlayer.setVideoURI(exercise.data?.videoUrl)
                    exerciseTitle.text = exercise.data?.name
                    exerciseHint.text = exercise.data?.exerciseHint
                }

                is Response.Failure -> {
                    createFailureResponse(Response.Failure(exercise.reason), requireContext())
                }
            }
        }
    }

    private fun initUi(view: View) {
        exerciseVideoPlayer = view.findViewById(R.id.exerciseVideoPlayer)
        exerciseTitle = view.findViewById(R.id.exerciseTitle)
        exerciseHint = view.findViewById(R.id.exerciseHint)
        exerciseHintFirstSet = view.findViewById(R.id.exerciseHintFirstSet)
        addNewSetBtn = view.findViewById(R.id.addNewSetBtn)
        exerciseDoneBtn = view.findViewById(R.id.exerciseDoneBtn)
        recyclerviewExerciseSetsDetails = view.findViewById(R.id.recyclerviewExerciseSetsDetails)
        recyclerviewExerciseSetsDetails.adapter = adapterRVExercisesResults
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_exercise, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.navigateTo(null)
    }
}