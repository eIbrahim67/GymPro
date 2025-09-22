package com.eibrahim67.gympro.myChats.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSourceImpl
import com.eibrahim67.gympro.databinding.FragmentMyChatsBinding
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.main.viewModel.MainViewModelFactory
import com.eibrahim67.gympro.myChats.model.Chat
import com.eibrahim67.gympro.myChats.model.UserChat
import com.eibrahim67.gympro.myChats.view.adapter.ChatsAdapter
import com.eibrahim67.gympro.myChats.viewModel.MyChatsViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MyChatsFragment : Fragment() {

    private var _binding: FragmentMyChatsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyChatsViewModel by viewModels()

    private val sharedViewModel: MainViewModel by activityViewModels {
        val remoteRepository =
            RemoteRepositoryImpl(RemoteDataSourceImpl(FirebaseFirestore.getInstance()))
        val dao = UserDatabase.Companion.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        MainViewModelFactory(userRepository, remoteRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Safe back button handler
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        val adapter = ChatsAdapter { chat, user ->
            chatWithTrainer(user.uid)
        }

        binding.recyclerviewChats.adapter = adapter
        viewModel.listenUserChats { chat ->
            viewModel.getUserInfo(chat.otherUid) { user ->
                adapter.updateChats(chat to user)
            }
        }

        binding.recyclerviewChats.adapter = adapter

    }

    private fun chatWithTrainer(id: String) {
        sharedViewModel.setChatWithId(id)
        sharedViewModel.navigateRightTo(R.id.action_chat)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}