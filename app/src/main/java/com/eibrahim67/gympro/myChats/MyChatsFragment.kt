package com.eibrahim67.gympro.myChats

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
import com.eibrahim67.gympro.databinding.FragmentMyChatsBinding
import com.eibrahim67.gympro.home.view.adapters.AdapterRVTrainers
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.myChats.adapter.ChatMessagesAdapter
import com.eibrahim67.gympro.train.viewModel.TrainViewModelFactory
import kotlin.getValue

class MyChatsFragment : Fragment() {

    private var _binding: FragmentMyChatsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyChatsViewModel by viewModels()

    private val sharedViewModel: MainViewModel by activityViewModels {
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        TrainViewModelFactory(userRepository)
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

        val sampleMessages = listOf(
            ChatMessage(1, "Ibrahim Mohamed", "بحب حبوبتي أوووووووووووووووووووووووووووي ♥\uFE0F", "3:30 PM", R.drawable.icon_solid_profile),
            ChatMessage(2, "Habiba  ♥\uFE0F", "حبوووووووووووبيييييي", "4:15 PM", R.drawable.icon_solid_profile)
        )

        val adapter = ChatMessagesAdapter(sampleMessages){id -> chatWithTrainer(id)}
        binding.recyclerviewChats.adapter = adapter

    }

    private fun chatWithTrainer(id: Int) {
        sharedViewModel.setChatWithId(id)
        sharedViewModel.navigateRightTo(R.id.action_chat)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
