package com.eibrahim67.gympro.utils.helperClass

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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ImageHandler(
    private val fragment: Fragment,
    private val onImagePicked: (File) -> Unit,
    private val onError: (String) -> Unit
) {
    private val context get() = fragment.requireContext()
    private var currentPermission: String? = null

    // ✅ New Android 13+ Photo Picker (no permission needed)
    private val photoPickerLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { processImage(it) } ?: onError("No image selected")
        }

    // ✅ Legacy GetContent() for < Android 13
    private val pickImageLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { processImage(it) } ?: onError("No image selected")
        }

    private val permissionLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            val permission = currentPermission ?: getStoragePermission()
            if (granted) pickImageLegacy()
            else if (!fragment.shouldShowRequestPermissionRationale(permission)) {
                showSettingsDialog()
            } else {
                showPermissionRationaleDialog(permission)
            }
        }

    fun requestImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // ✅ Use Photo Picker (no permission required)
            photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            // ✅ For older devices → need storage permission
            val permission = getStoragePermission()
            currentPermission = permission

            when {
                ContextCompat.checkSelfPermission(
                    context, permission
                ) == PackageManager.PERMISSION_GRANTED -> pickImageLegacy()

                fragment.shouldShowRequestPermissionRationale(permission) -> showPermissionRationaleDialog(
                    permission
                )

                else -> permissionLauncher.launch(permission)
            }
        }
    }

    private fun getStoragePermission(): String {
        return Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private fun pickImageLegacy() {
        pickImageLauncher.launch("image/*")
    }

    private fun processImage(uri: Uri) {
        fragment.lifecycleScope.launch {
            val file = File(context.cacheDir, "img_${System.currentTimeMillis()}.png")
            try {
                withContext(Dispatchers.IO) {
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        FileOutputStream(file).use { output -> input.copyTo(output) }
                    } ?: throw Exception("Unable to open image")
                }
                onImagePicked(file)
            } catch (e: Exception) {
                file.delete()
                onError("Image error: ${e.message}")
            }
        }
    }

    private fun showPermissionRationaleDialog(permission: String) {
        AlertDialog.Builder(context).setTitle("Storage Permission Required")
            .setMessage("We need access to your photos so you can upload images.")
            .setPositiveButton("Grant") { _, _ -> permissionLauncher.launch(permission) }
            .setNegativeButton("Cancel") { _, _ -> onError("Permission denied") }.show()
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(context).setTitle("Permission Required")
            .setMessage("Storage permission is permanently denied. Please enable it from settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                fragment.startActivity(intent)
            }.setNegativeButton("Cancel") { _, _ -> onError("Permission denied") }.show()
    }
}
