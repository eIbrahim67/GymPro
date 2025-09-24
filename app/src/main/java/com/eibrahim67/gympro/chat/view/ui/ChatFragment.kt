package com.eibrahim67.gympro.chat.view.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.chat.model.ChatMessage
import com.eibrahim67.gympro.chat.view.adapter.ChatAdapter
import com.eibrahim67.gympro.chat.viewModel.ChatViewModel
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSourceImpl
import com.eibrahim67.gympro.databinding.FragmentChatBinding
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.main.viewModel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    val auth = FirebaseAuth.getInstance()

    private val viewModel: ChatViewModel by viewModels()

    private val sharedViewModel: MainViewModel by activityViewModels {
        val remoteRepository =
            RemoteRepositoryImpl(RemoteDataSourceImpl(FirebaseFirestore.getInstance()))
        val dao = UserDatabase.Companion.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        MainViewModelFactory(userRepository, remoteRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.chatWithId.observe(viewLifecycleOwner) { id ->
            sharedViewModel.getCoachById(id)
        }

        Log.i("testAccount", sharedViewModel.chatWithId.value.toString())

        viewModel.getUserInfo(sharedViewModel.chatWithId.value) { user ->

            if (user == null) {
                Snackbar.make(view, "Something Wrong", Snackbar.LENGTH_LONG).show()
                findNavController().popBackStack()
                return@getUserInfo
            }

            Glide.with(view)
                .load(user.photoUrl)
                .error(R.drawable.icon_solid_profile)
                .into(binding.chatWithUserImage)
            binding.chatWithUserName.text = user.name

            viewModel.createOrGetChat(
                user.uid, auth.uid.toString()
            ) { onChatId ->
                Log.i("createOrGetChat", onChatId)
            }
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (s.toString().isEmpty()){
                    binding.sendButtonCard.visibility = View.GONE
                    binding.uploadImageButton.visibility = View.VISIBLE
                }else{
                    binding.sendButtonCard.visibility = View.VISIBLE
                    binding.uploadImageButton.visibility = View.GONE
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val adapter = ChatAdapter(auth.uid)
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.chatRecyclerView.adapter = adapter

        binding.sendButtonCard.setOnClickListener {
            val message = binding.inputEditText.text.toString()
            if (message.isNotBlank()) {
                val chatId = viewModel.chatId.value ?: return@setOnClickListener
                viewModel.sendMessage(chatId, message)

                binding.inputEditText.text?.clear()
            }
        }

        viewModel.chatId.observe(viewLifecycleOwner) { chatId ->

            viewModel.listenForMessages(chatId) { sender, text ->
                adapter.addMessage(
                    ChatMessage(
                        senderId = sender,
                        text = text,
                        type = "text",
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}