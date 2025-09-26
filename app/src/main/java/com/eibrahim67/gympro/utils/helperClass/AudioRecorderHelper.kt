package com.eibrahim67.gympro.utils.helperClass

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.io.File

class AudioRecorderHelper(
    private val fragment: Fragment,
    private val onStart: () -> Unit,
    private val onStop: () -> Unit,
    private val onAudioReady: (File) -> Unit,
    private val onError: (String) -> Unit
) {
    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null

    private val context get() = fragment.requireContext()

    private val permissionLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) toggleRecording()
            else if (!fragment.shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                showSettingsDialog()
            } else {
                showPermissionRationaleDialog()
            }
        }

    fun requestAudioPermission() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        } else {
            toggleRecording()
        }
    }

    private fun toggleRecording() {
        if (mediaRecorder != null) stopRecording()
        else startRecording()
    }

    private fun startRecording() {
        onStart()
        audioFile = File(context.cacheDir, "audio_${System.currentTimeMillis()}.mp3")
        try {
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFile!!.absolutePath)
                prepare()
                start()
            }
        } catch (e: Exception) {
            onError("Recording failed: ${e.message}")
        }
    }

    private fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            audioFile?.let { onAudioReady(it) } ?: onError("Audio file missing")
            onStop()
        } catch (e: Exception) {
            onError("Recording stop failed: ${e.message}")
        }
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(context).setTitle("Microphone Permission Required")
            .setMessage("We need access to your microphone so you can record audio messages.")
            .setPositiveButton("Grant") { _, _ ->
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }.setNegativeButton("Cancel") { _, _ -> onError("Permission denied") }.show()
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(context).setTitle("Permission Required")
            .setMessage("Microphone permission is permanently denied. Please enable it from settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                fragment.startActivity(intent)
            }.setNegativeButton("Cancel") { _, _ -> onError("Permission denied") }.show()
    }
}
