package com.eibrahim67.gympro.createPlan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
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
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import kotlin.math.absoluteValue

class CreatePlanFragment : Fragment() {

    private var _binding: FragmentCreatePlanBinding? = null
    private val binding get() = _binding!!

    private val difficulties = listOf("Beginner", "Intermediate", "Advanced")

    var selectedMuscleIds: List<Int>? = null
    var selectedWorkoutsIds: List<Int>? = null
    var selectedCategoriesIds: List<Int>? = null

    private var selectedImageUrl: String? = null


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

        viewModel.getAllWorkouts()
        viewModel.workouts.observe(viewLifecycleOwner) { workouts ->
            when (workouts) {
                is ResponseEI.Loading -> {
                    // Optionally show a loading indicator
                }

                is ResponseEI.Success -> {
                    val workoutsList = workouts.data // List<Workout?>
                    val workoutsNames = workoutsList.map { it?.name ?: "Unknown" }.toTypedArray()

                    binding.workoutsIdsAutoComplete.setOnClickListener {
                        showMultiSelectDialog(
                            title = "Select Workouts",
                            items = workoutsNames,
                            onSelected = { selectedIndices ->
                                // Safely get the selected workout IDs based on the selected indices
                                selectedWorkoutsIds = selectedIndices.mapNotNull { index ->
                                    workoutsList.getOrNull(index)?.id
                                }

                                val selectedNames = selectedIndices.mapNotNull { index ->
                                    workoutsList.getOrNull(index)?.name
                                }

                                binding.workoutsIdsAutoComplete.setText(
                                    selectedNames.joinToString(", ")
                                )
                            }
                        )
                    }
                }

                is ResponseEI.Failure -> {
                    // Optionally show error message
                }
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

                    val categoriesList = categories.data // List<Category?>
                    val categoriesNames =
                        categoriesList.map { it?.name ?: "Unknown" }.toTypedArray()

                    binding.trainingCategoriesIdsAutoComplete.setOnClickListener {
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
                        binding.root, "Plan under creation...", Snackbar.LENGTH_SHORT
                    ).show()

                }

                is ResponseEI.Success -> {

                    Snackbar.make(
                        binding.root, "Plan created successfully!", Snackbar.LENGTH_SHORT
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

        binding.uploadImageButton.setOnClickListener {

            requestStoragePermission()

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
                                val updatedPlan = plan.copy(
                                    coachId = it.id
                                )
                                viewModel.createPlan(updatedPlan)
                                viewModel.addTrainPlanId(user.data.id, updatedPlan.id)
                            }

                        }

                        is ResponseEI.Failure -> {}
                    }

                }

            }
        }

    }

    private fun createTrainPlan(): TrainPlan? {
        showSnackbar(selectedImageUrl.toString())
        val name = binding.planNameEditText.text.toString().trim()
        val description = binding.descriptionEditText.text.toString().trim()
        val duration = binding.durationDaysEditText.text.toString().toIntOrNull()
        val difficulty = binding.difficultyLevelAutoComplete.text.toString()

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
        if (selectedImageUrl.toString().isNullOrEmpty()) {
            Snackbar.make(requireView(), "Add image is required", Snackbar.LENGTH_LONG).show()
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
            id = generateUniqueIntId(),
            name = name,
            description = description,
            durationDaysPerTrainingWeek = duration,
            workoutsIds = selectedWorkoutsIds ?: listOf(),
            targetedMuscleIds = selectedMuscleIds ?: listOf(),
            coachId = -1,
            difficultyLevel = difficulty,
            imageUrl = selectedImageUrl.toString() ?: "",
            trainingCategoriesIds = selectedCategoriesIds ?: listOf(),
            avgTimeMinPerWorkout = avgTime
        )
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
                    .into(binding.imageFeaturePlan)
                uploadImageAndGetUrl(uri, "images/${UUID.randomUUID()}.jpg")
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
            }.addOnFailureListener { e ->
                Log.e("Upload", "Failed to get download URL", e)
                selectedImageUrl = null
                showSnackbar("Failed to get image URL.")
            }
        }.addOnFailureListener { e ->
            Log.e("Upload", "Upload failed", e)
            selectedImageUrl = null
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
