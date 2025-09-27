package com.eibrahim67.gympro.ui.chat.view.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.App
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.main.viewModel.MainViewModel
import com.eibrahim67.gympro.core.main.viewModel.MainViewModelFactory
import com.eibrahim67.gympro.databinding.FragmentChatBinding
import com.eibrahim67.gympro.ui.chat.view.adapter.ChatAdapter
import com.eibrahim67.gympro.ui.chat.viewModel.ChatViewModel
import com.eibrahim67.gympro.utils.helperClass.AudioRecorderUploader
import com.eibrahim67.gympro.utils.helperClass.ImagePickerUploader
import com.eibrahim67.gympro.utils.response.ResponseEI
import com.google.android.material.loadingindicator.LoadingIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("View binding is only valid between onCreateView and onDestroyView")



    val auth = FirebaseAuth.getInstance()
    private var selectedImageUrl: String? = null

    private lateinit var adapter: ChatAdapter
    private lateinit var imagePickerUploader: ImagePickerUploader
    private lateinit var audioHelper: AudioRecorderUploader
    private lateinit var popup: PopupMenu

    private var imageUploaded = false
    private var isRecording = false

    private val viewModel: ChatViewModel by viewModels()

    private val sharedViewModel: MainViewModel by activityViewModels {
        val app = requireActivity().application as App
        MainViewModelFactory(app.userRepository, app.remoteRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI(view)
        observeViewModel()
        onClick()

    }

    private fun initUI(view: View) {
        binding.loadingIndicator.runLoading()

        viewModel.getUserInfo(sharedViewModel.chatWithId.value) { user ->

            if (user == null) {
                showSnackbar("User not found!")
                findNavController().popBackStack()
                return@getUserInfo
            }

            Glide.with(view).load(user.photoUrl).error(R.drawable.ic_profile_2)
                .into(binding.chatWithUserImage)
            binding.chatWithUserName.text = user.name

            viewModel.getChatId(user.uid, auth.uid.toString())
        }

        adapter = ChatAdapter(auth.uid)
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.chatRecyclerView.adapter = adapter

        binding.chatRecyclerView.onDataLoaded {
            binding.loadingIndicator.stopLoading()
        }

        imagePickerUploader = ImagePickerUploader(fragment = this, onUploadSuccess = { url ->
            binding.uploadImageChat.setImageURI(null)
            Glide.with(requireContext()).load(url).into(binding.uploadImageChat)
            selectedImageUrl = url.toString()
            imageUploaded = true
            binding.uploadImageChatLayout.visibility = View.VISIBLE
            Snackbar.make(binding.root, "Uploaded Successfully!", Snackbar.LENGTH_SHORT).show()
        }, onUploadError = { error ->
            Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
            imageUploaded = false
            selectedImageUrl = null
            binding.uploadImageChatLayout.visibility = View.GONE
            Glide.with(requireContext()).load(R.drawable.error_ic).into(binding.uploadImageChat)
        }, onLoading = { isLoading ->
            imageUploaded = false
            selectedImageUrl = null
            binding.uploadImageChatLayout.visibility = View.VISIBLE
            Glide.with(requireContext()).load(R.color.primary_white).into(binding.uploadImageChat)
            if (isLoading) binding.loadingIndicator.runLoading() else binding.loadingIndicator.stopLoading()
        })

        audioHelper = AudioRecorderUploader(this) { result ->
            result.onSuccess { url ->
                viewModel.sendMessage("", null, isAudio = true, audioUrl = url)
                binding.loadingIndicator.stopLoading()
            }
            result.onFailure { e ->
                Toast.makeText(
                    requireContext(), "Audio upload failed: ${e.message}", Toast.LENGTH_SHORT
                ).show()
                binding.loadingIndicator.stopLoading()
            }
        }

        binding.recordButton.setOnClickListener {
            if (isRecording) {
                isRecording = false
                audioHelper.stopRecordingAndUpload()
                binding.loadingIndicator.runLoading()
                binding.lottieView.stopAnimation()
                binding.recordButton.icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.icon_outline_mic)

            } else {
                isRecording = true
                audioHelper.startRecording()
                binding.lottieView.runAnimation()
                binding.recordButton.icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.icon_outline_send)
            }
        }

        popup = PopupMenu(requireContext(), binding.btnMore)
        popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)

    }

    private fun observeViewModel() {

        sharedViewModel.chatWithId.observe(viewLifecycleOwner) { id ->
            sharedViewModel.getCoachById(id)
        }

        viewModel.chatId.observe(viewLifecycleOwner) { chatId ->
            val parts = chatId.split("_")
            viewModel.createOrGetChat(parts[0], parts[1], chatId, onSuccess = {
                viewModel.listenForMessages(chatId) { msg ->
                    adapter.addMessage(msg)
                    binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
                }
            }, onFailure = {
                showSnackbar("something wrong!")
                findNavController().popBackStack()
            })

        }

    }

    private fun onClick() {

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_clear_temporary -> {
                    adapter.clearMessages()
                    true
                }

                R.id.action_clear -> {
                    binding.loadingIndicator.runLoading()
                    viewModel.deleteOurChat(onSuccess = {
                        binding.loadingIndicator.stopLoading()
                        adapter.clearMessages()
                        showSnackbar("chat deleted successfully")
                    }, onFailure = {
                        binding.loadingIndicator.stopLoading()
                        showSnackbar("something wrong!")
                    })
                    true
                }

                else -> false
            }
        }

        binding.btnMore.setOnClickListener {
            popup.show()
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.sendButton.setOnClickListener {
            val message = binding.inputEditText.text.toString()
            if (message.isNotBlank()) {
                viewModel.sendMessage(message, null, false, null)
                binding.inputEditText.text?.clear()
            }
        }

        binding.sendImageButton.setOnClickListener {
            viewModel.sendMessage(
                "", selectedImageUrl ?: return@setOnClickListener, isAudio = false, audioUrl = null
            )
            selectedImageUrl = null
            imageUploaded = false
            binding.uploadImageChatLayout.visibility = View.GONE
        }

        binding.cancelImageButton.setOnClickListener {
            selectedImageUrl = null
            imageUploaded = false
            binding.uploadImageChatLayout.visibility = View.GONE
        }

        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (s.toString().isEmpty()) {
                    binding.sendButton.visibility = View.GONE
                    binding.recordButton.visibility = View.VISIBLE
                } else {
                    binding.sendButton.visibility = View.VISIBLE
                    binding.recordButton.visibility = View.GONE
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.uploadImageButton.setOnClickListener {
            imagePickerUploader.pickImage()
        }
    }

    fun LoadingIndicator.runLoading() {
        visibility = View.VISIBLE
    }

    fun LoadingIndicator.stopLoading() {
        visibility = View.GONE
    }

    fun LottieAnimationView.runAnimation() {
        visibility = View.VISIBLE
        playAnimation()
    }

    fun LottieAnimationView.stopAnimation() {
        visibility = View.GONE
        cancelAnimation()
    }

    fun showSnackbar(msg: String) {
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("ChatFragment", "onDestroyView")
    }

    override fun onDestroy() {
        Log.e("ChatFragment", "onDestroy")
        super.onDestroy()
    }

    fun RecyclerView.onDataLoaded(callback: () -> Unit) {
        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() = callback()
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = callback()
        }

        adapter?.registerAdapterDataObserver(observer)

        // unregister automatically when view is destroyed
        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                adapter?.unregisterAdapterDataObserver(observer)
            }
        })
    }


}