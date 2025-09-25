package com.eibrahim67.gympro.ui.becomeTrainer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.App
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.data.remote.model.Coach
import com.eibrahim67.gympro.databinding.FragmentBecomeTrainerBinding
import com.eibrahim67.gympro.ui.becomeTrainer.viewModel.BecomeTrainerViewModel
import com.eibrahim67.gympro.ui.becomeTrainer.viewModel.BecomeTrainerViewModelFactory
import com.eibrahim67.gympro.utils.helperClass.ImagePickerUploader
import com.eibrahim67.gympro.utils.response.ResponseEI
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class BecomeTrainerFragment : Fragment() {

    private var _binding: FragmentBecomeTrainerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BecomeTrainerViewModel by viewModels {
        val app = requireActivity().application as App
        BecomeTrainerViewModelFactory(app.remoteRepository)
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
                    val categoriesNames = categoriesList.map { it.name }.toTypedArray()

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
                                    selectedNames.joinToString(", ")
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

        val imagePickerUploader = ImagePickerUploader(fragment = this, onUploadSuccess = { url ->
            binding.imageCoach.setImageURI(null)
            Glide.with(requireContext()).load(url).into(binding.imageCoach)
            selectedImageUrl = url.toString()
            imageUploaded = true
            Snackbar.make(binding.root, "Uploaded Successfully!", Snackbar.LENGTH_SHORT).show()
        }, onUploadError = { error ->
            Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
            imageUploaded = false
            selectedImageUrl = null
            Glide.with(requireContext()).load(R.drawable.error_ic).into(binding.imageCoach)
        }, onLoading = { isLoading ->
            imageUploaded = false
            selectedImageUrl = null
            Glide.with(requireContext()).load(R.color.primary_white).into(binding.imageCoach)
            binding.loadingAnimation.apply {
                visibility = if (isLoading) View.VISIBLE else View.GONE
                if (isLoading) playAnimation() else cancelAnimation()
            }
        })

        binding.uploadImageButton.setOnClickListener {
            imagePickerUploader.pickImage()
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
            profileImageUrl = selectedImageUrl.toString(),
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
}