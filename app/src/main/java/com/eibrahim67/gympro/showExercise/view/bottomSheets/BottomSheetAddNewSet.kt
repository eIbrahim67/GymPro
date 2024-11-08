package com.eibrahim67.gympro.showExercise.view.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.utils.UtilsFunctions.createFailureResponse
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.train.viewModel.TrainViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText

class BottomSheetAddNewSet : BottomSheetDialogFragment() {

    private lateinit var itemExerciseImpSets: TextView
    private lateinit var itemExerciseImpReps: TextView
    private lateinit var itemExerciseWeight: TextInputEditText
    private lateinit var itemExerciseReps: TextInputEditText
    private lateinit var bottomSheetAddNewSetBtn: MaterialCardView

    private val sharedViewModel: MainViewModel by activityViewModels {
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        TrainViewModelFactory(userRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
        initListener()
        initObservers()
    }

    private fun initObservers() {
        sharedViewModel.fetchDateExerciseData()
        sharedViewModel.userDataExercise.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {}
                is Response.Success -> {}
                is Response.Failure -> {
                    createFailureResponse(Response.Failure(response.reason), requireContext())
                }
            }
        }
        sharedViewModel.updateUserExerciseState.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {}
                is Response.Success -> {}
                is Response.Failure -> {
                    createFailureResponse(Response.Failure(response.reason), requireContext())
                }
            }
        }
        sharedViewModel.updateUserExercise.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {}
                is Response.Success -> {
                    sharedViewModel.fetchDateExerciseData()
                }

                is Response.Failure -> {
                    createFailureResponse(Response.Failure(response.reason), requireContext())
                }
            }
        }
    }

    private fun initListener() {
        bottomSheetAddNewSetBtn.setOnClickListener {
            val weight = itemExerciseWeight.text.toString()
            val reps = itemExerciseReps.text.toString()
            sharedViewModel.exerciseId.value?.let { id ->
                sharedViewModel.updateUserExercise(id, weight, reps)
            }
            dismiss()
        }
    }

    private fun initUi(view: View) {
        itemExerciseImpSets = view.findViewById(R.id.itemExerciseImpSets)
        itemExerciseImpReps = view.findViewById(R.id.itemExerciseImpReps)
        itemExerciseWeight = view.findViewById(R.id.newExerciseWeight)
        itemExerciseReps = view.findViewById(R.id.newExerciseReps)
        bottomSheetAddNewSetBtn = view.findViewById(R.id.bottomSheetAddNewSetBtn)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_add_new_set_exercise, container, false)
    }

    companion object {
        const val TAG = "BOTTOM_SHEET_ADD_NEW_SET"
    }
}