package com.eibrahim67.gympro.becomeTrainer.view

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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.becomeTrainer.viewModel.BecomeTrainerViewModel
import com.eibrahim67.gympro.becomeTrainer.viewModel.BecomeTrainerViewModelFactory
import com.eibrahim67.gympro.core.data.remote.model.Coach
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSourceImpl
import com.eibrahim67.gympro.core.response.ResponseEI
import com.eibrahim67.gympro.databinding.FragmentBecomeTrainerBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class BecomeTrainerFragment : Fragment() {

    private var _binding: FragmentBecomeTrainerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BecomeTrainerViewModel by viewModels {
        val remoteRepository =
            RemoteRepositoryImpl(RemoteDataSourceImpl(FirebaseFirestore.getInstance()))

        BecomeTrainerViewModelFactory(remoteRepository)
    }

    var selectedCategoriesIds: List<Int>? = null

    private var selectedImageUrl: String? = null
    private var imageUploaded: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBecomeTrainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDropdowns()
        setupButtonListeners()
    }

    private fun setupDropdowns() {

        viewModel.getAllCategories()
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            when (categories) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {

                    val categoriesList = categories.data
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

        viewModel.addedCoach.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Loading -> {

                    Snackbar.make(
                        binding.root, "Applying under processing...", Snackbar.LENGTH_SHORT
                    ).show()

                }

                is ResponseEI.Success -> {

                    Snackbar.make(
                        binding.root, "Applied as a Coach successfully!", Snackbar.LENGTH_SHORT
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

        binding.uploadImageButton.setOnClickListener {
            requestStoragePermission()
        }


        binding.applyBtn.setOnClickListener {
            val coach = addCoach()
            if (coach != null) {
                viewModel.addCoach(coach)
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

    private fun addCoach(): Coach? {
        showSnackbar(selectedImageUrl.toString())
        val name = binding.updateCoachName.text.toString().trim()
        val bio = binding.updateBioCoach.text.toString().trim()
        val email = binding.updateCoachEmail.text.toString().trim()
        val phone = binding.updateCoachPhone.text.toString().trim()
        val experience = binding.updateCoachExperienceYears.text.toString().toIntOrNull()
        val price = binding.updateCoachPrice.text.toString().toDoubleOrNull()

        // Validation
        if (name.isEmpty()) {
            binding.updateCoachName.error = "Coach name is required"
            return null
        }
        if (bio.isEmpty()) {
            binding.updateBioCoach.error = "Coach Bio is required"
            return null
        }
        if (email.isEmpty()) {
            binding.updateCoachEmail.error = "Valid Coach email is required"
            return null
        }
        if (phone.isEmpty()) {
            binding.updateCoachPhone.error = "Coach phone is required"
            return null
        }
        if (experience == null || experience <= 0) {
            binding.updateCoachExperienceYears.error = "experience is required"
            return null
        }
        if (price == null || price <= 0) {
            binding.updateCoachPrice.error = "Subscription Price is required"
            return null
        }
        if (selectedImageUrl.toString().isEmpty()) {
            Snackbar.make(requireView(), "Add image is required", Snackbar.LENGTH_LONG).show()
            return null
        }
        if (selectedCategoriesIds.isNullOrEmpty()) {
            binding.trainingCategoriesIds.error = "Select at least one category"
            return null
        }
        if (!imageUploaded) {
            Snackbar.make(
                requireView(), "Wait until image upload successfully", Snackbar.LENGTH_LONG
            ).show()
            return null
        }
        return Coach(
            id = FirebaseAuth.getInstance().uid.toString(),
            name = name,
            bio = bio,
            profileImageUrl = selectedImageUrl.toString() ?: "",
            specializationIds = selectedCategoriesIds ?: listOf(),
            experienceYears = experience,
            certifications = null,
            contactEmail = email,
            contactPhone = phone,
            price = price,
            rate = 0.0
        )
    }

    fun showSnackbar(msg: String) {
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var currentStoragePermission: String? = null

    private fun requestStoragePermission() {
        currentStoragePermission = getStoragePermission()
        val permission = currentStoragePermission!!

        when {
            ContextCompat.checkSelfPermission(
                requireContext(), permission
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
                Glide.with(requireContext()).load(uri).into(binding.imageFeaturePlan)
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
        AlertDialog.Builder(requireContext()).setTitle("Storage Permission Required")
            .setMessage("This app needs access to your photos to upload images. Please grant the permission.")
            .setPositiveButton("Grant") { _, _ -> permissionLauncher.launch(currentStoragePermission) }
            .setNegativeButton("Cancel") { _, _ -> showSnackbar("Permission denied") }
            .setCancelable(false).show()
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
            imageUploaded = false
            selectedImageUrl = null
            showSnackbar("Image upload failed. Please try again.")
        }
    }

}