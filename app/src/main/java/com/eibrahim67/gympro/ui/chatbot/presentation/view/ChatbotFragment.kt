package com.eibrahim67.gympro.ui.chatbot.presentation.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.databinding.FragmentChatbotBinding
import com.eibrahim67.gympro.ui.chatbot.data.network.ChatLlamaStreamProcessor
import com.eibrahim67.gympro.ui.chatbot.data.network.HttpClient
import com.eibrahim67.gympro.ui.chatbot.domain.repositoryImpl.ChatRepositoryImpl
import com.eibrahim67.gympro.ui.chatbot.domain.usecase.GetChatResponseUseCase
import com.eibrahim67.gympro.ui.chatbot.presentation.view.adapter.ChatbotAdapter
import com.eibrahim67.gympro.ui.chatbot.presentation.viewModel.ChatbotViewModel
import com.eibrahim67.gympro.ui.chatbot.presentation.viewModel.ChatbotViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ChatbotFragment : Fragment() {

    private var _binding: FragmentChatbotBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatbotAdapter: ChatbotAdapter
    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null

    private val viewModel: ChatbotViewModel by viewModels {
        ChatbotViewModelFactory(
            GetChatResponseUseCase(
                ChatRepositoryImpl(ChatLlamaStreamProcessor(HttpClient()))
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatbotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { processImage(it) }
        }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            handlePermissionResult(granted, getStoragePermission())
        }

    private val recordAudioLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) toggleRecording()
            else showToast("Microphone permission denied")
        }


    override fun onDestroyView() {
        super.onDestroyView()
        stopRecording()
        _binding = null
    }

    private fun setupRecyclerView() {
        chatbotAdapter = ChatbotAdapter()
        binding.chatRecyclerView.apply {
            adapter = chatbotAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupListeners() {
        binding.uploadImageButton.setOnClickListener {
            requestStoragePermission()
        }

        binding.recordButton.setOnClickListener {
            requestAudioPermission()
        }

        binding.sendButtonCard.setOnClickListener {
            val message = binding.inputEditText.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.startChat(message)
                binding.inputEditText.text.clear()
            }
        }


        binding.backLayout.setOnClickListener {

            findNavController().popBackStack()

        }

        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateSendButtonVisibility(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            chatbotAdapter.updateData(state.messages)
            binding.chatRecyclerView.scrollToPosition(state.messages.size - 1)
            binding.recordButton.visibility =
                if (state.isSendButtonVisible) View.GONE else View.VISIBLE

            state.errorMessage?.let { showToast(it) }

            binding.recordButton.setImageResource(
                if (state.isRecording) R.drawable.ic_stop_recording
                else R.drawable.icon_outline_mic
            )
        }
    }

    private fun processImage(uri: Uri) {
        lifecycleScope.launch {
            val file =
                File(requireContext().cacheDir, "temp_image_${System.currentTimeMillis()}.png")
            try {
                withContext(Dispatchers.IO) {
                    requireContext().contentResolver.openInputStream(uri)?.use { input ->
                        FileOutputStream(file).use { output ->
                            input.copyTo(output)
                        }
                    } ?: throw Exception("Unable to open image.")
                }
                viewModel.processImage(file)
            } catch (e: Exception) {
                showToast("Image error: ${e.message}")
                file.delete()
            }
        }
    }

    private fun requestStoragePermission() {
        val permission = getStoragePermission()
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> pickImage()

            shouldShowRequestPermissionRationale(permission) -> showPermissionRationaleDialog()
            else -> permissionLauncher.launch(permission)
        }
    }

    private fun getStoragePermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private fun requestAudioPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
        } else {
            toggleRecording()
        }
    }


    private fun handlePermissionResult(granted: Boolean, permission: String) {
        if (granted) {
            pickImage()
        } else if (!shouldShowRequestPermissionRationale(permission)) {
            showSettingsDialog()
        } else {
            showToast("Permission denied")
        }
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Storage Permission Required")
            .setMessage("Access to photos is needed to upload images for OCR.")
            .setPositiveButton("Grant") { _, _ -> permissionLauncher.launch(getStoragePermission()) }
            .setNegativeButton("Cancel") { _, _ -> showToast("Permission denied") }
            .setCancelable(false)
            .show()
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Required")
            .setMessage("Please enable storage permission in settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", requireContext().packageName, null)
                })
            }
            .setNegativeButton("Cancel") { _, _ -> showToast("Permission denied") }
            .setCancelable(false)
            .show()
    }

    private fun pickImage() {
        pickImageLauncher.launch("image/*")
    }

    private fun toggleRecording() {
        if (viewModel.uiState.value?.isRecording == true) stopRecording()
        else startRecording()
    }

    private fun startRecording() {
        audioFile = File(requireContext().cacheDir, "audio_${System.currentTimeMillis()}.mp3")
        try {
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFile!!.absolutePath)
                prepare()
                start()
            }
            viewModel.setRecordingState(true)
            showToast("Recording started")
        } catch (e: Exception) {
            showToast("Recording failed: ${e.message}")
        }
    }

    private fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            viewModel.setRecordingState(false)
            showToast("Recording stopped")

            audioFile?.let {
                viewModel.processAudio(it)
            } ?: showToast("Audio file missing")
        } catch (e: Exception) {
            showToast("Recording failed: ${e.message}")
        }
    }

    private fun showToast(message: String) {
        if (isAdded) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }
}
