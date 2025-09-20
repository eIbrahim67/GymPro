package com.eibrahim67.gympro.showExercise.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSourceImpl
import com.eibrahim67.gympro.core.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions.createFailureResponse
import com.eibrahim67.gympro.databinding.FragmentExerciseBinding
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.main.viewModel.MainViewModelFactory
import com.eibrahim67.gympro.showExercise.view.adapters.AdapterRVExercisesResults
import com.eibrahim67.gympro.showExercise.view.bottomSheets.BottomSheetAddNewSet
import com.google.firebase.firestore.FirebaseFirestore

class ExerciseFragment : Fragment() {

    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!

    private val adapterRVExercisesCurrent = AdapterRVExercisesResults()
    private val adapterRVExercisesHistory = AdapterRVExercisesResults()

    private val sharedViewModel: MainViewModel by activityViewModels {
        val remoteRepository =
            RemoteRepositoryImpl(RemoteDataSourceImpl(FirebaseFirestore.getInstance()))
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        MainViewModelFactory(userRepository, remoteRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initListeners()
        initObservers()
    }

    private fun initUi() = with(binding) {
        recyclerviewExerciseSetsDetails.adapter = adapterRVExercisesCurrent
        recyclerviewExerciseHistory.adapter = adapterRVExercisesHistory
    }

    private fun initListeners() = with(binding) {
        addNewSetBtn.setOnClickListener {
            BottomSheetAddNewSet().show(
                requireActivity().supportFragmentManager, BottomSheetAddNewSet.TAG
            )
        }

        exerciseBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        exerciseHistoryBtn.setOnClickListener {
            if (exerciseHistory.isVisible) {
                exerciseHistory.visibility = View.GONE
                exerciseDoneBtnLayout.visibility = View.VISIBLE
                exerciseHistoryBtnSrc.setImageResource(R.drawable.icon_history)
            } else {
                exerciseHistory.visibility = View.VISIBLE
                exerciseDoneBtnLayout.visibility = View.GONE
                exerciseHistoryBtnSrc.setImageResource(R.drawable.icon_close)
            }
        }
    }

    private fun initObservers() {
        sharedViewModel.exerciseId.observe(viewLifecycleOwner) { id ->
            if (id != null) {
                sharedViewModel.getExerciseById(id)
            }
        }

        sharedViewModel.exerciseById.observe(viewLifecycleOwner) { exercise ->
            when (exercise) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    exercise.data?.let { data ->
                        // Load image safely with Glide
                        Glide.with(requireContext()).load(data.imageUrl ?: "").apply(
                                RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.color.gray_v1)
                                    .error(R.drawable.error_ic)
                            ).into(binding.exerciseImage)

                        binding.exerciseTitle.text = data.name ?: getString(R.string.no_name)
                        binding.exerciseHint.text = data.exerciseHint ?: getString(R.string.no_hint)

                        Toast.makeText(
                            requireContext(), data.imageUrl ?: "No image url", Toast.LENGTH_SHORT
                        ).show()
                    } ?: run {
                        Toast.makeText(
                            requireContext(), "Exercise data not found", Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is ResponseEI.Failure -> {
                    createFailureResponse(ResponseEI.Failure(exercise.reason), requireContext())
                }
            }
        }

        sharedViewModel.updateUserExercise.observe(viewLifecycleOwner) {
            sharedViewModel.fetchDateExerciseData()
        }

        sharedViewModel.userDataExercise.observe(viewLifecycleOwner) { userDataExercise ->
            when (userDataExercise) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    val currentUserDataExercise =
                        userDataExercise.data[sharedViewModel.exerciseId.value]

                    if (!currentUserDataExercise.isNullOrEmpty()) {
                        adapterRVExercisesHistory.submitList(currentUserDataExercise)
                        adapterRVExercisesCurrent.submitList(currentUserDataExercise)
                        binding.exerciseHintFirstSet.text =
                            getText(R.string.your_exercise_sets_details)
                    } else {
                        binding.exerciseHintFirstSet.text = getText(R.string.no_exercise_sets)
                    }
                }

                is ResponseEI.Failure -> {
                    createFailureResponse(
                        ResponseEI.Failure(userDataExercise.reason), requireContext()
                    )
                }
            }
        }

        sharedViewModel.fetchDateExerciseData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        sharedViewModel.navigateRightTo(null)
    }
}
