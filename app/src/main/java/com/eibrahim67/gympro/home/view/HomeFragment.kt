package com.eibrahim67.gympro.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSourceImpl
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.utils.UtilsFunctions
import com.eibrahim67.gympro.home.view.adapters.AdapterRVCategories
import com.eibrahim67.gympro.home.view.adapters.AdapterRVFeaturedPlans
import com.eibrahim67.gympro.home.view.adapters.AdapterRVOtherWorkouts
import com.eibrahim67.gympro.home.view.adapters.AdapterRVTrainers
import com.eibrahim67.gympro.home.viewModel.HomeViewModel
import com.eibrahim67.gympro.home.viewModel.HomeViewModelFactory
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.train.viewModel.TrainViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {


    private lateinit var recyclerviewCategories: RecyclerView
    private lateinit var recyclerviewFeaturePlan: RecyclerView
    private lateinit var recyclerviewOurTrainers: RecyclerView
    private lateinit var recyclerviewOthersWorkout: RecyclerView
    private lateinit var currentDate: TextView
    private lateinit var textViewHello: TextView

    private lateinit var bottomNavigationView: BottomNavigationView

    private val adapterRVFeaturedPlans = AdapterRVFeaturedPlans() { id -> goToTrainPlan(id) }
    private val adapterRVTrainers = AdapterRVTrainers()
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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
        updateUi()
        initObservers()
    }

    private fun updateUi() {
        recyclerviewCategories.adapter = adapterRVCategories
        recyclerviewFeaturePlan.adapter = adapterRVFeaturedPlans
        recyclerviewOurTrainers.adapter = adapterRVTrainers
        recyclerviewOthersWorkout.adapter = adapterRVOtherWorkouts
    }

    private fun initUi(view: View) {
        recyclerviewCategories = view.findViewById(R.id.recyclerviewCategories)
        recyclerviewFeaturePlan = view.findViewById(R.id.recyclerviewFeaturePlan)
        recyclerviewOurTrainers = view.findViewById(R.id.recyclerviewOurTrainers)
        recyclerviewOthersWorkout =
            view.findViewById(R.id.recyclerviewOthersWorkout)

        textViewHello = view.findViewById(R.id.textViewHello)
        currentDate = view.findViewById(R.id.currentDate)

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.VISIBLE
    }

    private fun initObservers() {
        viewModel.getCategories()

        viewModel.categories.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Response.Loading -> {

                }

                is Response.Success -> {
                    response.data?.let { adapterRVCategories.submitList(it) }
                }

                is Response.Failure -> {

                }
            }
        }

//        viewModel.getCategories()
//
//        viewModel.categories.observe(viewLifecycleOwner) { data ->
//            if (data != null && data.isNotEmpty()) {
//                adapterRVCategories.submitList(data)
//            } else {
//                Log.e("Category", "No categories found or an error occurred")
//                //TODO:Add some design
//            }
//        }


        sharedViewModel.getTrainPlans()

        sharedViewModel.trainPlans.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Response.Loading -> {

                }

                is Response.Success -> {
                    adapterRVFeaturedPlans.submitList(response.data)
                }

                is Response.Failure -> {
                    utils.createFailureResponse(response, requireContext())
                }
            }

        }

        viewModel.getCoaches()

        viewModel.coaches.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Response.Loading -> {

                }

                is Response.Success -> {
                    adapterRVTrainers.submitList(response.data)
                }

                is Response.Failure -> {
                    utils.createFailureResponse(response, requireContext())
                }
            }

        }

        viewModel.getWorkouts()

        viewModel.workouts.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Response.Loading -> {

                }

                is Response.Success -> {
                    adapterRVOtherWorkouts.submitList(response.data)
                }

                is Response.Failure -> {
                    utils.createFailureResponse(response, requireContext())
                }
            }

        }

        viewModel.getCurrentDate()
        viewModel.currentDate.observe(viewLifecycleOwner) { date ->


            when (date) {
                is Response.Loading -> {}

                is Response.Success -> {
                    currentDate.text = date.data
                }

                is Response.Failure -> {
                    utils.createFailureResponse(date, requireContext())
                }
            }

        }

        viewModel.getHelloSate()
        viewModel.helloSate.observe(viewLifecycleOwner) { date ->


            when (date) {
                is Response.Loading -> {}

                is Response.Success -> {
                    textViewHello.text = date.data
                }

                is Response.Failure -> {
                    utils.createFailureResponse(date, requireContext())
                }
            }

        }
    }

    private fun goToTrainPlan(id: Int) {
        sharedViewModel.setTrainPlanId(id)
        sharedViewModel.navigateTo(R.id.action_showTrainPlan)
    }

}