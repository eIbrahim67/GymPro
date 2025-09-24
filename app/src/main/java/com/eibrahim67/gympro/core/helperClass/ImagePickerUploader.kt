package com.eibrahim67.gympro.core.helperClass

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class ImagePickerUploader(
    private val fragment: Fragment,
    private val onUploadSuccess: (String) -> Unit,
    private val onUploadError: (String) -> Unit,
    private val onLoading: (Boolean) -> Unit
) {
    private var selectedImageUrl: String? = null

    // Photo Picker (Android 13+)
    private val pickImageWithPhotoPicker =
        fragment.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { handleImageSelection(it) }
        }

    // Legacy picker (older Android)
    private val pickImageWithContent =
        fragment.registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { handleImageSelection(it) }
        }

    // Permission launcher
    private val permissionLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) pickImageLegacy()
            else if (!fragment.shouldShowRequestPermissionRationale(getLegacyStoragePermission())) {
                showSettingsDialog()
            } else {
                onUploadError("Permission denied")
            }
        }

    fun pickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pickImageWithPhotoPicker.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } else {
            requestLegacyStoragePermission()
        }
    }

    private fun handleImageSelection(uri: Uri) {
        selectedImageUrl = null
        onLoading(true)

        val storageRef = FirebaseStorage.getInstance()
            .reference.child("images/${UUID.randomUUID()}.jpg")

        storageRef.putFile(uri)
            .addOnSuccessListener {
                storageRef.downloadUrl
                    .addOnSuccessListener { url ->
                        selectedImageUrl = url.toString()
                        onLoading(false)
                        onUploadSuccess(url.toString())
                    }
                    .addOnFailureListener { e ->
                        onLoading(false)
                        onUploadError("Failed to get image URL: ${e.localizedMessage}")
                    }
            }
            .addOnFailureListener { e ->
                onLoading(false)
                onUploadError("Upload failed: ${e.localizedMessage}")
            }
    }

    private fun requestLegacyStoragePermission() {
        val permission = getLegacyStoragePermission()
        when {
            ContextCompat.checkSelfPermission(fragment.requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
                pickImageLegacy()
            }
            fragment.shouldShowRequestPermissionRationale(permission) -> {
                showPermissionRationaleDialog(permission)
            }
            else -> {
                permissionLauncher.launch(permission)
            }
        }
    }

    private fun pickImageLegacy() {
        pickImageWithContent.launch("image/*")
    }

    private fun getLegacyStoragePermission(): String =
        Manifest.permission.READ_EXTERNAL_STORAGE

    private fun showPermissionRationaleDialog(permission: String) {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle("Storage Permission Required")
            .setMessage("This app needs access to your photos to upload images. Please grant the permission.")
            .setPositiveButton("Grant") { _, _ -> permissionLauncher.launch(permission) }
            .setNegativeButton("Cancel") { _, _ -> onUploadError("Permission denied") }
            .show()
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle("Permission Required")
            .setMessage("Storage permission is needed to upload images. Please enable it in the app settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                fragment.startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", fragment.requireContext().packageName, null)
                    }
                )
            }
            .setNegativeButton("Cancel") { _, _ -> onUploadError("Permission denied") }
            .show()
    }
}
