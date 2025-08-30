package com.eibrahim67.gympro.createWorkout

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
import com.eibrahim67.gympro.core.data.remote.model.Workout
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSourceImpl
import com.eibrahim67.gympro.core.response.ResponseEI
import com.eibrahim67.gympro.databinding.FragmentCreateWorkoutBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import kotlin.math.absoluteValue

class CreateWorkoutFragment : Fragment() {

    private var _binding: FragmentCreateWorkoutBinding? = null
    private val binding get() = _binding!!

    private val difficulties = listOf("Beginner", "Intermediate", "Advanced")

    var selectedMuscleIds: List<Int>? = null
    var selectedExerciseIds: List<Int>? = null

    private var selectedImageUrl: String? = null
    private var imageUploaded: Boolean = false

    private val viewModel: CreateWorkoutViewModel by viewModels {
        val remoteRepository =
            RemoteRepositoryImpl(RemoteDataSourceImpl(FirebaseFirestore.getInstance()))

        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        CreateWorkoutViewModelFactory(remoteRepository, userRepository)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateWorkoutBinding.inflate(inflater, container, false)
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


        viewModel.getAllExercises()
        viewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            when (exercises) {
                is ResponseEI.Loading -> {
                    // Optionally show a loading indicator
                }

                is ResponseEI.Success -> {
                    val exercisesList = exercises.data // List<Workout?>
                    val exercisesNames = exercisesList.map { it?.name ?: "Unknown" }.toTypedArray()

                    binding.exerciseIdsAutoComplete.setOnClickListener {
                        showMultiSelectDialog(
                            title = "Select Exercises",
                            items = exercisesNames,
                            onSelected = { selectedIndices ->
                                // Safely get the selected exercise IDs based on the selected indices
                                selectedExerciseIds = selectedIndices.mapNotNull { index ->
                                    exercisesList.getOrNull(index)?.id
                                }

                                val selectedNames = selectedIndices.mapNotNull { index ->
                                    exercisesList.getOrNull(index)?.name
                                }

                                binding.exerciseIdsAutoComplete.setText(
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

        viewModel.createWorkout.observe(viewLifecycleOwner) { createWorkout ->
            when (createWorkout) {
                is ResponseEI.Loading -> {

                    Snackbar.make(
                        binding.root, "Workout under creation...", Snackbar.LENGTH_SHORT
                    ).show()

                }

                is ResponseEI.Success -> {

                    Snackbar.make(
                        binding.root, "Workout created successfully!", Snackbar.LENGTH_SHORT
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
            AlertDialog.Builder(requireContext()).setTitle("Workout History")
                .setMessage("View your previously created workouts.").setPositiveButton("OK", null)
                .show()
        }

        binding.uploadImageButton.setOnClickListener {

            requestStoragePermission()

        }


        binding.createWorkoutButton.setOnClickListener {
            val workout = createWorkout()

            if (workout != null) {
                viewModel.getLoggedInUser()
                viewModel.loggedInUser.observe(viewLifecycleOwner) { user ->

                    when (user) {
                        is ResponseEI.Loading -> {
                        }

                        is ResponseEI.Success -> {

                            user.data?.let { it ->
                                val updatedWorkout = workout.copy(
                                    coachId = it.id
                                )
                                viewModel.createWorkout(updatedWorkout)
                                viewModel.addWorkoutId(user.data.id, updatedWorkout.id)
                            }

                        }

                        is ResponseEI.Failure -> {}
                    }

                }

            }
        }

    }

    private fun createWorkout(): Workout? {
        val name = binding.workoutNameEditText.text.toString().trim()
        val description = binding.descriptionEditText.text.toString().trim()
        val duration = binding.durationEditText.text.toString().toIntOrNull()
        val difficulty = binding.difficultyLevelAutoComplete.text.toString()
        val equipments = binding.equipmentEditText.text.toString()

        // Validation
        if (name.isEmpty()) {
            binding.workoutName.error = "Plan name is required"
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
        if (selectedExerciseIds.isNullOrEmpty()) {
            binding.exerciseIds.error = "Select at least one workout"
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
        if (equipments.toString().isEmpty()) {
            binding.equipments.error = "Add equipments is required"
            return null
        }
        if (!imageUploaded){
            Snackbar.make(requireView(), "Wait until image upload successfully", Snackbar.LENGTH_LONG).show()
            return null
        }


        return Workout(
            id = generateUniqueIntId(),
            name = name,
            description = description,
            durationMinutes = duration,
            exerciseIds = selectedExerciseIds ?: listOf(),
            targetedMuscleIds = selectedMuscleIds ?: listOf(),
            coachId = -1,
            difficultyLevel = difficulty,
            imageUrl = selectedImageUrl.toString() ?: "",
            equipment = equipments
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
                    .into(binding.imageFeatureWorkout)
                uploadImageAndGetUrl(uri, "images/${UUID.randomUUID()}.jpg")
                imageUploaded = false
                binding.loadingAnimation.playAnimation()
                binding.loadingAnimation.visibility = View.VISIBLE
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
                binding.loadingAnimation.cancelAnimation()
                binding.loadingAnimation.visibility = View.GONE
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