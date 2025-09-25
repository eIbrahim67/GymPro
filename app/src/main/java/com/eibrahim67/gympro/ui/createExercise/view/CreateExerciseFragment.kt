package com.eibrahim67.gympro.ui.createExercise.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.App
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.data.remote.model.Exercise
import com.eibrahim67.gympro.databinding.FragmentCreateExcerciseBinding
import com.eibrahim67.gympro.ui.createExercise.viewModel.CreateExerciseViewModel
import com.eibrahim67.gympro.ui.createExercise.viewModel.CreateExerciseViewModelFactory
import com.eibrahim67.gympro.utils.helperClass.ImagePickerUploader
import com.eibrahim67.gympro.utils.response.ResponseEI
import com.google.android.material.snackbar.Snackbar
import java.util.UUID
import kotlin.math.absoluteValue

class CreateExerciseFragment : Fragment() {

    private var _binding: FragmentCreateExcerciseBinding? = null
    private val binding get() = _binding!!

    private val difficulties = listOf("Beginner", "Intermediate", "Advanced")

    var selectedMuscleIds: List<Int>? = null
    var selectedCategoriesIds: List<Int>? = null

    private var selectedImageUrl: String? = null
    private var imageUploaded: Boolean = false

    private val viewModel: CreateExerciseViewModel by viewModels {
        val app = requireActivity().application as App
        CreateExerciseViewModelFactory(app.remoteRepository, app.userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateExcerciseBinding.inflate(inflater, container, false)
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
                is ResponseEI.Loading -> {
                    // Optional: Show loading indicator
                }

                is ResponseEI.Success -> {
                    val muscleList = response.data  // List<Muscle?>
                    val muscleNames = muscleList.map { it?.name ?: "Unknown" }.toTypedArray()

                    binding.targetedMuscleIdsAutoComplete.setOnClickListener {
                        showMultiSelectDialog(
                            title = "Select Targeted Muscles",
                            items = muscleNames,
                            onSelected = { selectedIndices ->
                                selectedMuscleIds = selectedIndices.mapNotNull { index ->
                                    muscleList.getOrNull(index)?.id
                                }

                                val selectedNames = selectedIndices.mapNotNull { index ->
                                    muscleList.getOrNull(index)?.name
                                }

                                binding.targetedMuscleIdsAutoComplete.setText(
                                    selectedNames.joinToString(", ")
                                )
                            })
                    }
                }

                is ResponseEI.Failure -> {
                    // Optional: Show error message
                }
            }
        }


        viewModel.getAllCategories()
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            when (categories) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {

                    val categoriesList = categories.data // List<Category?>
                    val categoriesNames =
                        categoriesList.map { it?.name ?: "Unknown" }.toTypedArray()

                    binding.categoryIdsAutoComplete.setOnClickListener {
                        showMultiSelectDialog(
                            title = "Select categories",
                            items = categoriesNames,
                            onSelected = { selectedIndices ->

                                selectedCategoriesIds = selectedIndices.mapNotNull { index ->
                                    categoriesList.getOrNull(index)?.id
                                }

                                val selectedNames = selectedIndices.mapNotNull { index ->
                                    categoriesList.getOrNull(index)?.name
                                }

                                binding.categoryIdsAutoComplete.setText(
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

        viewModel.createExercise.observe(viewLifecycleOwner) { createExercise ->
            when (createExercise) {
                is ResponseEI.Loading -> {

                    Snackbar.make(
                        binding.root, "Exercise under creation...", Snackbar.LENGTH_SHORT
                    ).show()

                }

                is ResponseEI.Success -> {

                    Snackbar.make(
                        binding.root, "Exercise created successfully!", Snackbar.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
                }

                is ResponseEI.Failure -> {}
            }
        }


    }

    private fun setupButtonListeners() {
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.moreBtn.setOnClickListener {
            AlertDialog.Builder(requireContext()).setTitle("Exercise History")
                .setMessage("View your previously created exercises.").setPositiveButton("OK", null)
                .show()
        }

        val imagePickerUploader = ImagePickerUploader(fragment = this, onUploadSuccess = { url ->
            binding.imageFeatureExercise.setImageURI(null)
            Glide.with(requireContext()).load(url).into(binding.imageFeatureExercise)
            selectedImageUrl = url.toString()
            imageUploaded = true
            Snackbar.make(binding.root, "Uploaded Successfully!", Snackbar.LENGTH_SHORT).show()
        }, onUploadError = { error ->
            Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
            imageUploaded = false
            selectedImageUrl = null
            Glide.with(requireContext()).load(R.drawable.error_ic)
                .into(binding.imageFeatureExercise)
        }, onLoading = { isLoading ->
            imageUploaded = false
            selectedImageUrl = null
            Glide.with(requireContext()).load(R.color.primary_white)
                .into(binding.imageFeatureExercise)
            binding.loadingAnimation.apply {
                visibility = if (isLoading) View.VISIBLE else View.GONE
                if (isLoading) playAnimation() else cancelAnimation()
            }
        })

        binding.uploadImageButton.setOnClickListener {
            imagePickerUploader.pickImage()
        }


        binding.createExerciseButton.setOnClickListener {
            val exercise = createExercise()

            if (exercise != null) {
                viewModel.getLoggedInUser()
                viewModel.loggedInUser.observe(viewLifecycleOwner) { user ->

                    when (user) {
                        is ResponseEI.Loading -> {
                        }

                        is ResponseEI.Success -> {

                            user.data?.let { it ->
                                val updatedExercise = exercise.copy(
                                    coachId = it.id
                                )
                                viewModel.createExercise(updatedExercise)
                                viewModel.addExerciseId(user.data.id, updatedExercise.id)
                            }

                        }

                        is ResponseEI.Failure -> {}
                    }

                }

            }
        }

    }

    private fun createExercise(): Exercise? {
        val name = binding.exerciseNameEditText.text.toString().trim()
        val description = binding.descriptionEditText.text.toString().trim()
        val hint = binding.exerciseHintEditText.text.toString()
        val reps = binding.exerciseRepsEditText.text.toString().toIntOrNull()
        val sets = binding.exerciseSetEditText.text.toString().toIntOrNull()
        val difficulty = binding.difficultyLevelAutoComplete.text.toString()

        // Validation
        if (name.isEmpty()) {
            binding.exerciseName.error = "Plan name is required"
            return null
        }
        if (description.isEmpty()) {
            binding.description.error = "Description is required"
            return null
        }
        if (hint.isEmpty()) {
            binding.exerciseHint.error = "Hint is required"
            return null
        }
        if (reps == null || reps <= 0) {
            binding.exerciseReps.error = "Valid reps is required"
            return null
        }
        if (sets == null || sets <= 0) {
            binding.exerciseSet.error = "Valid sets is required"
            return null
        }
        if (selectedCategoriesIds.isNullOrEmpty()) {
            binding.categoryIds.error = "Select at least one category"
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
        if (selectedImageUrl.toString().isEmpty()) {
            Snackbar.make(requireView(), "Add image is required", Snackbar.LENGTH_LONG).show()
            return null
        }
        if (!imageUploaded) {
            Snackbar.make(
                requireView(), "Wait until image upload successfully", Snackbar.LENGTH_LONG
            ).show()
            return null
        }


        return Exercise(
            id = generateUniqueIntId(),
            name = name,
            description = description,
            exerciseHint = hint,
            exerciseSet = sets,
            exerciseReps = reps,
            categoryIds = selectedCategoriesIds ?: listOf(),
            effectedMusclesIds = selectedMuscleIds ?: listOf(),
            coachId = -1,
            exerciseIntensity = 1,
            imageUrl = selectedImageUrl.toString() ?: "",
            videoUrl = null
        )
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

    fun generateUniqueIntId(): Int {
        return UUID.randomUUID().toString().hashCode().absoluteValue
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showSnackbar(msg: String) {
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT).show()
    }

}