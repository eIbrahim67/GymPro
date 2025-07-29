package com.eibrahim67.gympro.showExercise.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions.createFailureResponse
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.showExercise.view.adapters.AdapterRVExercisesResults
import com.eibrahim67.gympro.showExercise.view.bottomSheets.BottomSheetAddNewSet
import com.eibrahim67.gympro.train.viewModel.TrainViewModelFactory
import com.google.android.material.card.MaterialCardView

class ExerciseFragment : Fragment() {

    private lateinit var exerciseImage: ImageView
    private lateinit var exerciseHistoryBtnSrc: ImageView
    private lateinit var exerciseTitle: TextView
    private lateinit var exerciseHint: TextView
    private lateinit var exerciseHintFirstSet: TextView
    private lateinit var recyclerviewExerciseSetsDetails: RecyclerView
    private lateinit var recyclerviewExerciseHistory: RecyclerView
    private lateinit var exerciseDoneBtn: MaterialCardView
    private lateinit var addNewSetBtn: MaterialCardView
    private lateinit var backBtn: MaterialCardView
    private lateinit var exerciseHistory: MaterialCardView

    private val adapterRVExercisesCurrent = AdapterRVExercisesResults()
    private val adapterRVExercisesHistory = AdapterRVExercisesResults()

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
        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        exerciseHistoryBtnSrc.setOnClickListener {
            if (exerciseHistory.visibility == View.VISIBLE) {
                exerciseHistory.visibility = View.GONE
                exerciseHistoryBtnSrc.setImageResource(R.drawable.icon_history)
            } else {
                exerciseHistory.visibility = View.VISIBLE
                exerciseHistoryBtnSrc.setImageResource(R.drawable.icon_close)
            }
        }
    }

    private fun initObservers() {
        sharedViewModel.fetchDateExerciseData()
        sharedViewModel.exerciseId.observe(viewLifecycleOwner) { id ->
            sharedViewModel.getExerciseById(id)
        }
        sharedViewModel.userDataExercise.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    val list = response.data[sharedViewModel.exerciseId.value]
                    if (!list.isNullOrEmpty()) {
                        adapterRVExercisesHistory.submitList(list)
                        exerciseHintFirstSet.text = getText(R.string.your_exercise_sets_details)
                    }
                }

                is ResponseEI.Failure -> {}
            }
        }

        sharedViewModel.exerciseById.observe(viewLifecycleOwner) { exercise ->
            when (exercise) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    Glide.with(requireContext())
                        .load(exercise.data?.imageUrl).into(exerciseImage)
                    exerciseTitle.text = exercise.data?.name
                    exerciseHint.text = exercise.data?.exerciseHint
                    Toast.makeText(requireContext(), exercise.data?.imageUrl, Toast.LENGTH_SHORT).show()
                }

                is ResponseEI.Failure -> {
                    createFailureResponse(ResponseEI.Failure(exercise.reason), requireContext())
                }
            }
        }
    }

    private fun initUi(view: View) {
        exerciseImage = view.findViewById(R.id.exerciseImage)
        exerciseHistoryBtnSrc = view.findViewById(R.id.exerciseHistoryBtnSrc)
        exerciseTitle = view.findViewById(R.id.exerciseTitle)
        exerciseHint = view.findViewById(R.id.exerciseHint)
        exerciseHintFirstSet = view.findViewById(R.id.exerciseHintFirstSet)
        addNewSetBtn = view.findViewById(R.id.addNewSetBtn)
        exerciseDoneBtn = view.findViewById(R.id.exerciseDoneBtn)
        backBtn = view.findViewById(R.id.exerciseBackBtn)
        exerciseHistory = view.findViewById(R.id.exerciseHistory)

        recyclerviewExerciseSetsDetails = view.findViewById(R.id.recyclerviewExerciseSetsDetails)
        recyclerviewExerciseSetsDetails.adapter = adapterRVExercisesCurrent

        recyclerviewExerciseHistory = view.findViewById(R.id.recyclerviewExerciseHistory)
        recyclerviewExerciseHistory.adapter = adapterRVExercisesHistory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_exercise, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.navigateRightTo(null)
    }
}