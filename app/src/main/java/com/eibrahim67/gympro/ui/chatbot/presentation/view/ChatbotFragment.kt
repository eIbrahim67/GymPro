package com.eibrahim67.gympro.ui.chatbot.presentation.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.databinding.FragmentChatbotBinding
import com.eibrahim67.gympro.ui.chatbot.data.network.llm.ChatLlamaStreamProcessor
import com.eibrahim67.gympro.ui.chatbot.data.network.llm.HttpClient
import com.eibrahim67.gympro.ui.chatbot.domain.repositoryImpl.ChatRepositoryImpl
import com.eibrahim67.gympro.ui.chatbot.domain.usecase.GetChatResponseUseCase
import com.eibrahim67.gympro.ui.chatbot.presentation.view.adapter.ChatbotAdapter
import com.eibrahim67.gympro.ui.chatbot.presentation.viewModel.ChatbotViewModel
import com.eibrahim67.gympro.ui.chatbot.presentation.viewModel.ChatbotViewModelFactory
import com.eibrahim67.gympro.utils.helperClass.AudioRecorderHelper
import com.eibrahim67.gympro.utils.helperClass.ImageHandler

class ChatbotFragment : Fragment() {

    private var _binding: FragmentChatbotBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatbotAdapter: ChatbotAdapter

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

    override fun onDestroyView() {
        super.onDestroyView()
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

        val imageHandler = ImageHandler(
            fragment = this,
            onImagePicked = { file -> viewModel.processImage(file) },
            onError = { msg -> showToast(msg) })

        val audioHandler = AudioRecorderHelper(
            fragment = this,
            onStart = {


                Glide.with(requireContext()).asGif().load(R.drawable.mic_record) // in res/drawable
                    .into(binding.playerView)
                binding.playerViewLayout.visibility = View.VISIBLE

            },
            onStop = {
                binding.playerViewLayout.visibility = View.GONE
            },
            onAudioReady = { file -> viewModel.processAudio(file) },
            onError = { msg -> showToast(msg) })

        binding.uploadImageButton.setOnClickListener {
            imageHandler.requestImage()
        }

        binding.recordButton.setOnClickListener {
            audioHandler.requestAudioPermission()
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

    private fun showToast(message: String) {
        if (isAdded) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }
}
