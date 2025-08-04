package com.eibrahim67.gympro.createPlan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSourceImpl
import com.eibrahim67.gympro.core.data.response.ResponseEI
import com.eibrahim67.gympro.databinding.FragmentCreatePlanBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class CreatePlanFragment : Fragment() {

    private var _binding: FragmentCreatePlanBinding? = null
    private val binding get() = _binding!!

    private val difficulties = listOf("Beginner", "Intermediate", "Advanced")

    var selectedMuscleIds: List<Int>? = null
    var selectedWorkoutsIds: List<Int>? = null
    var selectedCategoriesIds: List<Int>? = null

    private val viewModel: CreatePlanViewModel by viewModels {
        val remoteRepository =
            RemoteRepositoryImpl(RemoteDataSourceImpl(FirebaseFirestore.getInstance()))

        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        CreatePlanViewModelFactory(remoteRepository, userRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDropdowns()
        setupButtonListeners()
    }

    private fun setupDropdowns() {

        viewModel.getAllMuscles()
        viewModel.muscles.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    val muscleMap = response.data  // Map<Int, Muscles?>
                    val muscleNames = muscleMap.map { it.value?.name.toString() }
                        .toTypedArray() // Use muscle name if available
                    binding.targetedMuscleIdsAutoComplete.setOnClickListener {
                        showMultiSelectDialog(
                            title = "Select Targeted Muscles",
                            items = muscleNames,
                            onSelected = { selectedIndices ->
                                selectedMuscleIds =
                                    selectedIndices.map { muscleMap.keys.toList()[it] }
                                val selectedNames =
                                    selectedMuscleIds?.map { muscleMap.get(it)?.name }
                                binding.targetedMuscleIdsAutoComplete.setText(
                                    selectedNames?.joinToString(", ")
                                )
                            })
                    }
                }

                is ResponseEI.Failure -> {}
            }
        }

        viewModel.getAllWorkouts()
        viewModel.workouts.observe(viewLifecycleOwner) { workouts ->
            when (workouts) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    val workoutsMap = workouts.data  // Map<Int, Workouts?>
                    val workoutsNames = workoutsMap.map { it.value?.name.toString() }
                        .toTypedArray() // Use workouts name if available
                    binding.workoutsIdsAutoComplete.setOnClickListener {
                        showMultiSelectDialog(
                            title = "Select Workouts",
                            items = workoutsNames,
                            onSelected = { selectedIndices ->
                                selectedWorkoutsIds =
                                    selectedIndices.map { workoutsMap.keys.toList()[it] }
                                val selectedNames =
                                    selectedWorkoutsIds?.map { workoutsMap.get(it)?.name }
                                binding.workoutsIdsAutoComplete.setText(
                                    selectedNames?.joinToString(", ")
                                )
                            })
                    }
                }

                is ResponseEI.Failure -> {}
            }
        }


        // Difficulty single-select
        val difficultyAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, difficulties)
        binding.difficultyLevelAutoComplete.setAdapter(difficultyAdapter)

        viewModel.getAllCategories()
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            when (categories) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    val categoriesMap = categories.data  // Map<Int, Category?>
                    val categoriesNames = categoriesMap.map { it.value?.name.toString() }
                        .toTypedArray() // Use categories name if available
                    binding.trainingCategoriesIdsAutoComplete.setOnClickListener {
                        showMultiSelectDialog(
                            title = "Select categories",
                            items = categoriesNames,
                            onSelected = { selectedIndices ->
                                selectedCategoriesIds =
                                    selectedIndices.map { categoriesMap.keys.toList()[it] }
                                val selectedNames =
                                    selectedCategoriesIds?.map { categoriesMap.get(it)?.name }
                                binding.trainingCategoriesIdsAutoComplete.setText(
                                    selectedNames?.joinToString(", ")
                                )
                            })
                    }
                }

                is ResponseEI.Failure -> {}
            }
        }
        viewModel.createPlan.observe(viewLifecycleOwner) { createPlan ->
            when (createPlan) {
                is ResponseEI.Loading -> {

                    Snackbar.make(
                        binding.root,
                        "Plan under creation...",
                        Snackbar.LENGTH_SHORT
                    ).show()

                }

                is ResponseEI.Success -> {

                    Snackbar.make(
                        binding.root,
                        "Plan created successfully!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
                }

                is ResponseEI.Failure -> {}
            }
        }


    }

    private fun showMultiSelectDialog(
        title: String, items: Array<String>, onSelected: (List<Int>) -> Unit
    ) {
        val selectedItems = BooleanArray(items.size) { false }
        AlertDialog.Builder(requireContext()).setTitle(title)
            .setMultiChoiceItems(items, selectedItems) { _, which, isChecked ->
                selectedItems[which] = isChecked
            }.setPositiveButton("OK") { _, _ ->
                val selectedIndices = selectedItems.indices.filter { selectedItems[it] }
                onSelected(selectedIndices)
            }.setNegativeButton("Cancel", null).show()
    }

    private fun setupButtonListeners() {
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.moreBtn.setOnClickListener {
            AlertDialog.Builder(requireContext()).setTitle("Plan History")
                .setMessage("View your previously created plans.").setPositiveButton("OK", null)
                .show()
        }

        binding.createPlanButton.setOnClickListener {
            val plan = createTrainPlan()

            if (plan != null) {
                viewModel.getLoggedInUser()
                viewModel.loggedInUser.observe(viewLifecycleOwner) { user ->

                    when (user) {
                        is ResponseEI.Loading -> {
                        }

                        is ResponseEI.Success -> {

                            user.data?.let { it ->
                                val updatedPlan =
                                    plan.copy(
                                        coachId = it.id
                                    )
                                viewModel.createPlan(updatedPlan)
                            }

                        }

                        is ResponseEI.Failure -> {}
                    }

                }

            }
        }
    }

    private fun createTrainPlan(): TrainPlan? {
        val name = binding.planNameEditText.text.toString().trim()
        val description = binding.descriptionEditText.text.toString().trim()
        val duration = binding.durationDaysEditText.text.toString().toIntOrNull()
        val difficulty = binding.difficultyLevelAutoComplete.text.toString()
        val imageUrl = binding.imageUrlEditText.text.toString().trim()
        val avgTime = binding.avgTimeMinEditText.text.toString().toIntOrNull()

        // Validation
        if (name.isEmpty()) {
            binding.planName.error = "Plan name is required"
            return null
        }
        if (description.isEmpty()) {
            binding.description.error = "Description is required"
            return null
        }
        if (duration == null || duration <= 0) {
            binding.durationDays.error = "Valid duration is required"
            return null
        }
        if (selectedWorkoutsIds.isNullOrEmpty()) {
            binding.workoutsIds.error = "Select at least one workout"
            return null
        }
        if (selectedMuscleIds.isNullOrEmpty()) {
            binding.targetedMuscleIds.error = "Select at least one muscle"
            return null
        }
        if (difficulty.isEmpty() || difficulty !in difficulties) {
            binding.difficultyLevel.error = "Select a difficulty level"
            return null
        }
        if (imageUrl.isEmpty() || !imageUrl.startsWith("http")) {
            binding.imageUrl.error = "Valid image URL is required"
            return null
        }
        if (selectedCategoriesIds.isNullOrEmpty()) {
            binding.trainingCategoriesIds.error = "Select at least one category"
            return null
        }
        if (avgTime == null || avgTime <= 0) {
            binding.avgTimeMin.error = "Valid average time is required"
            return null
        }

        return TrainPlan(
            id = (Date().time).toInt(),
            name = name,
            description = description,
            durationDaysPerTrainingWeek = duration,
            workoutsIds = selectedWorkoutsIds ?: listOf(),
            targetedMuscleIds = selectedMuscleIds ?: listOf(),
            coachId = -1,
            difficultyLevel = difficulty,
            imageUrl = imageUrl,
            trainingCategoriesIds = selectedCategoriesIds ?: listOf(),
            avgTimeMinPerWorkout = avgTime
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}