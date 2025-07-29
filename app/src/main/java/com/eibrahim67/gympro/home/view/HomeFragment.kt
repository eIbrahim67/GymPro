package com.eibrahim67.gympro.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.databinding.FragmentHomeBinding
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSourceImpl
import com.eibrahim67.gympro.core.data.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions
import com.eibrahim67.gympro.home.view.adapters.*
import com.eibrahim67.gympro.home.viewModel.HomeViewModel
import com.eibrahim67.gympro.home.viewModel.HomeViewModelFactory
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.train.viewModel.TrainViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapterRVFeaturedPlans = AdapterRVFeaturedPlans { id -> goToTrainPlan(id) }
    private val adapterRVTrainers = AdapterRVTrainers{id -> chatWithTrainer(id) }
    private val adapterRVCategories = AdapterRVCategories()
    private val adapterRVOtherWorkouts = AdapterRVOtherWorkouts()

    private val utils = UtilsFunctions

    private val viewModel: HomeViewModel by viewModels {
        val remoteRepository =
            RemoteRepositoryImpl(RemoteDataSourceImpl(FirebaseFirestore.getInstance()))
        HomeViewModelFactory(remoteRepository)
    }

    private val sharedViewModel: MainViewModel by activityViewModels {
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        TrainViewModelFactory(userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        updateUi()
        initObservers()
    }

    private fun initUi() {
        binding.beginTraining.setOnClickListener {
            sharedViewModel.navigateRightTo(R.id.action_train)
        }

        binding.chatbotLayout.setOnClickListener {
            sharedViewModel.navigateRightTo(R.id.action_action_home_to_chatbot)
        }

        binding.profileLayout.setOnClickListener {

            sharedViewModel.navigateLeftTo(R.id.action_profile)

        }

        sharedViewModel.getMyTrainPlan()
        sharedViewModel.myTrainPlan.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    binding.todayWorkoutTitle.text = response.data?.name
                }
                is ResponseEI.Failure -> {}
            }
        }
    }

    private fun updateUi() {
        binding.recyclerviewFeaturePlan.adapter = adapterRVFeaturedPlans
        binding.recyclerviewOurTrainers.adapter = adapterRVTrainers

    }

    private fun initObservers() {
        viewModel.getCategories()
        viewModel.categories.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Success -> response.data?.let { adapterRVCategories.submitList(it) }
                is ResponseEI.Failure -> utils.createFailureResponse(response, requireContext())
                else -> {}
            }
        }

        sharedViewModel.getTrainPlans()
        sharedViewModel.trainPlans.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Success -> adapterRVFeaturedPlans.submitList(response.data)
                is ResponseEI.Failure -> utils.createFailureResponse(response, requireContext())
                else -> {}
            }
        }

        viewModel.getCoaches()
        viewModel.coaches.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Success -> adapterRVTrainers.submitList(response.data)
                is ResponseEI.Failure -> utils.createFailureResponse(response, requireContext())
                else -> {}
            }
        }

        viewModel.getWorkouts()
        viewModel.workouts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Success -> adapterRVOtherWorkouts.submitList(response.data)
                is ResponseEI.Failure -> utils.createFailureResponse(response, requireContext())
                else -> {}
            }
        }

        viewModel.getCurrentDate()
        viewModel.currentDate.observe(viewLifecycleOwner) { date ->
            when (date) {
                is ResponseEI.Success -> binding.currentDate.text = date.data
                is ResponseEI.Failure -> utils.createFailureResponse(date, requireContext())
                else -> {}
            }
        }

        viewModel.getHelloSate()
        viewModel.helloSate.observe(viewLifecycleOwner) { date ->
            when (date) {
                is ResponseEI.Success -> binding.textViewHello.text = date.data
                is ResponseEI.Failure -> utils.createFailureResponse(date, requireContext())
                else -> {}
            }
        }
    }

    private fun goToTrainPlan(id: Int) {
        sharedViewModel.setTrainPlanId(id)
        sharedViewModel.navigateRightTo(R.id.action_showTrainPlan)
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
