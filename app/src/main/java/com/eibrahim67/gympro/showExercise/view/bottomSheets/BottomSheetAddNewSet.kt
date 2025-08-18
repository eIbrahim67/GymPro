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
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSourceImpl
import com.eibrahim67.gympro.core.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions.createFailureResponse
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.main.viewModel.MainViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class BottomSheetAddNewSet : BottomSheetDialogFragment() {

    private lateinit var itemExerciseImpSets: TextView
    private lateinit var itemExerciseImpReps: TextView
    private lateinit var itemExerciseWeight: TextInputEditText
    private lateinit var itemExerciseReps: TextInputEditText
    private lateinit var bottomSheetAddNewSetBtn: MaterialCardView

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
        initListener()
        initObservers()
    }

    private fun initObservers() {

    }

    private fun initListener() {
        bottomSheetAddNewSetBtn.setOnClickListener {

            val weight = itemExerciseWeight.text.toString()
            val reps = itemExerciseReps.text.toString()

            if (weight.isNotEmpty() && reps.isNotEmpty()) {

                sharedViewModel.userDataExercise.value?.let { userDataExercise ->

                    when (userDataExercise) {
                        is ResponseEI.Loading -> {}

                        is ResponseEI.Success -> {

                            var updatedMap = mutableMapOf<Int, MutableList<String>>()
                                updatedMap = userDataExercise.data as MutableMap<Int, MutableList<String>>

                            updatedMap.getOrPut(sharedViewModel.exerciseId.value ?: -1) { mutableListOf() }.add("$weight#$reps")
                            sharedViewModel.updateExerciseMap(updatedMap)
                        }

                        is ResponseEI.Failure -> {
                        }
                    }

                }
                dismiss()
            }
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_add_new_set_exercise, container, false)
    }

    companion object {
        const val TAG = "BOTTOM_SHEET_ADD_NEW_SET"
    }
}