package com.eibrahim67.gympro.utils.helperClass

import android.Manifest
import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AudioRecorderUploader(
    private val fragment: Fragment,
    private val onResult: (Result<String>) -> Unit
) {
    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null

    // Launcher for asking RECORD_AUDIO permission
    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startRecordingInternal()
            } else {
                Toast.makeText(fragment.requireContext(), "Microphone permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    /** Call this to start recording (asks permission if needed) */
    fun startRecording() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startRecordingInternal()
            return
        }

        when {
            fragment.requireContext().checkSelfPermission(Manifest.permission.RECORD_AUDIO) ==
                    android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                startRecordingInternal()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    /** Stop recording and upload to Firebase */
    fun stopRecordingAndUpload() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null

            audioFile?.let { file ->
                uploadToFirebase(file)
            } ?: run {
                onResult(Result.failure(Exception("No audio file found")))
            }
        } catch (e: Exception) {
            onResult(Result.failure(e))
        }
    }

    private fun startRecordingInternal() {
        val context: Context = fragment.requireContext()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        audioFile = File(context.cacheDir, "AUDIO_$timeStamp.m4a")

        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context).apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFile!!.absolutePath)
                prepare()
                start()
            }
        } else {
            MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFile!!.absolutePath)
                prepare()
                start()
            }
        }
    }

    private fun uploadToFirebase(file: File) {
        val storageRef = FirebaseStorage.getInstance().reference
            .child("chat_audios/${file.name}")

        CoroutineScope(Dispatchers.IO).launch {
            val uploadTask = storageRef.putFile(android.net.Uri.fromFile(file))
            uploadTask.addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    onResult(Result.success(uri.toString()))
                }.addOnFailureListener {
                    onResult(Result.failure(it))
                }
            }.addOnFailureListener {
                onResult(Result.failure(it))
            }
        }
    }
}
