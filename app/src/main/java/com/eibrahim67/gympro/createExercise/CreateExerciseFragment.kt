package com.eibrahim67.gympro.createExercise

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.remote.model.Exercise
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSourceImpl
import com.eibrahim67.gympro.core.response.ResponseEI
import com.eibrahim67.gympro.databinding.FragmentCreateExcerciseBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
        val remoteRepository =
            RemoteRepositoryImpl(RemoteDataSourceImpl(FirebaseFirestore.getInstance()))

        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        CreateExerciseViewModelFactory(remoteRepository, userRepository)
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
                            }
                        )
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

        binding.uploadImageButton.setOnClickListener {

            requestStoragePermission()

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
        if (!imageUploaded){
            Snackbar.make(requireView(), "Wait until image upload successfully", Snackbar.LENGTH_LONG).show()
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

    private var currentStoragePermission: String? = null

    private fun requestStoragePermission() {
        currentStoragePermission = getStoragePermission()
        val permission = currentStoragePermission!!

        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> pickImage()

            shouldShowRequestPermissionRationale(permission) -> showPermissionRationaleDialog()

            else -> permissionLauncher.launch(permission)
        }
    }

    private fun getStoragePermission(): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

    private fun pickImage() {
        selectImageFromGalleryLauncher.launch("image/*")
    }

    private val selectImageFromGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                Glide.with(requireContext()).load(uri)
                    .into(binding.imageFeatureExercise)
                uploadImageAndGetUrl(uri, "images/${UUID.randomUUID()}.jpg")
                imageUploaded = false
            }
        }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            val permission = currentStoragePermission ?: getStoragePermission()
            handlePermissionResult(granted, permission)
        }

    private fun handlePermissionResult(granted: Boolean, permission: String) {
        if (granted) {
            pickImage()
        } else if (!shouldShowRequestPermissionRationale(permission)) {
            showSettingsDialog()
        } else {
            showSnackbar("Storage permission denied")
        }
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Storage Permission Required")
            .setMessage("This app needs access to your photos to upload images. Please grant the permission.")
            .setPositiveButton("Grant") { _, _ -> permissionLauncher.launch(currentStoragePermission) }
            .setNegativeButton("Cancel") { _, _ -> showSnackbar("Permission denied") }
            .setCancelable(false)
            .show()
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Required")
            .setMessage("Storage permission is needed to upload images. Please enable it in the app settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", requireContext().packageName, null)
                })
            }
            .setNegativeButton("Cancel") { _, _ -> showSnackbar("Permission denied") }
            .setCancelable(false)
            .show()
    }

    fun uploadImageAndGetUrl(imageUri: Uri, storagePath: String) {
        val storageRef = FirebaseStorage.getInstance().reference.child(storagePath)

        storageRef.putFile(imageUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                selectedImageUrl = uri.toString()
                Log.d("Upload", "Image uploaded: $selectedImageUrl")
                imageUploaded = true
            }.addOnFailureListener { e ->
                Log.e("Upload", "Failed to get download URL", e)
                selectedImageUrl = null
                imageUploaded = false
                showSnackbar("Failed to get image URL.")
            }
        }.addOnFailureListener { e ->
            Log.e("Upload", "Upload failed", e)
            selectedImageUrl = null
            imageUploaded = false
            showSnackbar("Image upload failed. Please try again.")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showSnackbar(msg: String) {
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT).show()
    }

}