package com.eibrahim67.gympro.chat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.chat.viewModel.ChatsViewModel
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSourceImpl
import com.eibrahim67.gympro.core.data.response.ResponseEI
import com.eibrahim67.gympro.databinding.FragmentChatBinding
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.main.viewModel.MainViewModelFactory
import com.eibrahim67.gympro.train.viewModel.TrainViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatsViewModel by viewModels()

    private val sharedViewModel: MainViewModel by activityViewModels {
        val remoteRepository =
            RemoteRepositoryImpl(RemoteDataSourceImpl(FirebaseFirestore.getInstance()))
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        MainViewModelFactory(userRepository, remoteRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.chatWithId.observe(viewLifecycleOwner) { id ->

            sharedViewModel.getCoachById(id)

        }

        sharedViewModel.coachById.observe(viewLifecycleOwner) { coach ->
            when (coach) {
                is ResponseEI.Loading -> {

                }
                is ResponseEI.Success -> {

                    Glide.with(view).load(coach.data?.profileImageUrl).into(binding.chatWithUserImage)
                    binding.chatWithUserName.text = coach.data?.name
                }

                is ResponseEI.Failure -> {
                    Snackbar.make(view, "Something Wrong", Snackbar.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }
            }

        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }


        // TODO: Use viewModel to load and display chat messages
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
