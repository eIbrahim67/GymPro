package com.eibrahim67.gympro.ui.showExercise.view.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.eibrahim67.gympro.App
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.main.viewModel.MainViewModel
import com.eibrahim67.gympro.core.main.viewModel.MainViewModelFactory
import com.eibrahim67.gympro.databinding.BottomSheetAddNewSetExerciseBinding
import com.eibrahim67.gympro.utils.response.ResponseEI
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetAddNewSet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddNewSetExerciseBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: MainViewModel by activityViewModels {
        val app = requireActivity().application as App
        MainViewModelFactory(app.userRepository, app.remoteRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddNewSetExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() = with(binding) {
        bottomSheetAddNewSetBtn.setOnClickListener {
            val weight = newExerciseWeight.text?.toString()?.trim()
            val reps = newExerciseReps.text?.toString()?.trim()

            if (weight.isNullOrEmpty() || reps.isNullOrEmpty()) {
                Toast.makeText(requireContext(), R.string.enter_weight_reps, Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            sharedViewModel.userDataExercise.value?.let { userDataExercise ->
                when (userDataExercise) {
                    is ResponseEI.Loading -> {
                        Toast.makeText(requireContext(), R.string.loading, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is ResponseEI.Success -> {
                        try {
                            val updatedMap =
                                (userDataExercise.data as? MutableMap<Int, MutableList<String>>)
                                    ?: mutableMapOf()

                            val exerciseId = sharedViewModel.exerciseId.value ?: -1
                            if (exerciseId == -1) {
                                Toast.makeText(
                                    requireContext(),
                                    R.string.invalid_exercise_id,
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@setOnClickListener
                            }

                            updatedMap.getOrPut(exerciseId) { mutableListOf() }.add("$weight#$reps")

                            sharedViewModel.updateExerciseMap(updatedMap)
                            dismiss()

                        } catch (e: Exception) {
                            Toast.makeText(
                                requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    is ResponseEI.Failure -> {
                        Toast.makeText(requireContext(), R.string.update_failed, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } ?: run {
                Toast.makeText(requireContext(), R.string.no_exercise_data, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "BOTTOM_SHEET_ADD_NEW_SET"
    }
}
