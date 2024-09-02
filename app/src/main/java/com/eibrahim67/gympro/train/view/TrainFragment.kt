package com.eibrahim67.gympro.train.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.response.Response
import com.eibrahim67.gympro.core.utils.UtilsFunctions
import com.eibrahim67.gympro.mainActivity.viewModel.MainViewModel
import com.eibrahim67.gympro.train.view.adapters.AdapterRVWorkouts
import com.eibrahim67.gympro.train.viewModel.TrainViewModel
import com.eibrahim67.gympro.train.viewModel.TrainViewModelFactory

class TrainFragment : Fragment() {

    private lateinit var recyclerviewWorkoutsExercises: RecyclerView

    private val adapterRVWorkouts = AdapterRVWorkouts()

    private val utils = UtilsFunctions

    private val viewModel: TrainViewModel by viewModels {
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        TrainViewModelFactory(userRepository)
    }

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
        return inflater.inflate(R.layout.fragment_train, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi(view)

        initObservers()
    }

    private fun initUi(view: View) {
        recyclerviewWorkoutsExercises = view.findViewById(R.id.recyclerviewWorkoutsExercises)
        recyclerviewWorkoutsExercises.adapter = adapterRVWorkouts
    }

    private fun initObservers() {

        viewModel.isUserHaveTrainer()

        viewModel.userHaveTrainer.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Response.Loading -> {

                }

                is Response.Success -> {

                    if (response.data) {

                        sharedViewModel.getMyTrainPlan()

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Show massage to suggest to user get a trainer",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        sharedViewModel.getMyTrainPlan()
                        //TODO: Show massage to suggest to user get a trainer
                    }

                }

                is Response.Failure -> {
                    utils.createFailureResponse(response, requireContext())
                }
            }

        }

        sharedViewModel.myTrainPlan.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Response.Loading -> {

                }

                is Response.Success -> {

                    adapterRVWorkouts.submitList(response.data.workoutsList)

                }

                is Response.Failure -> {
                    utils.createFailureResponse(response, requireContext())
                }
            }
        }
    }

}