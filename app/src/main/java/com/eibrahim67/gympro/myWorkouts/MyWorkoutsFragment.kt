package com.eibrahim67.gympro.myWorkouts

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSourceImpl
import com.eibrahim67.gympro.core.response.ResponseEI
import com.eibrahim67.gympro.databinding.FragmentMyWorkoutsBinding
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.main.viewModel.MainViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.getValue

class MyWorkoutsFragment : Fragment() {

    private var _binding: FragmentMyWorkoutsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyWorkoutsViewModel by viewModels{
        val remoteRepository =
            RemoteRepositoryImpl(RemoteDataSourceImpl(FirebaseFirestore.getInstance()))
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        MyWorkoutsViewModelFactory(remoteRepository, userRepository)
    }
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
        _binding = FragmentMyWorkoutsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addNewWorkout.setOnClickListener {
            sharedViewModel.navigateRightTo(R.id.action_createWorkoutFragment)
        }
        val adapterMyWorkouts = AdapterMyWorkouts { id -> goToWorkout(id) }
        binding.recyclerviewWorkouts.adapter = adapterMyWorkouts

        viewModel.getLoggedInUser()
        viewModel.loggedInUser.observe(viewLifecycleOwner) { user ->

            when (user) {
                is ResponseEI.Loading -> {

                }

                is ResponseEI.Success -> {

                    user.data?.let { viewModel.getMyWorkoutsIds(it.id) }

                }

                is ResponseEI.Failure -> {

                }
            }

        }

        viewModel.myWorkoutsIds.observe(viewLifecycleOwner) { myTrainPlansIds ->

            when (myTrainPlansIds) {
                is ResponseEI.Loading -> {
                }

                is ResponseEI.Success -> {


                    myTrainPlansIds.data?.let {
                        viewModel.getWorkouts(it)
                    }

                }

                is ResponseEI.Failure -> {}
            }

        }

        viewModel.workouts.observe(viewLifecycleOwner) { workouts ->

            when (workouts) {
                is ResponseEI.Loading -> {
                }

                is ResponseEI.Success -> {
                    workouts.data?.let { adapterMyWorkouts.submitList(workouts.data) }

                }

                is ResponseEI.Failure -> {
                }
            }

        }


    }

    private fun goToWorkout(id: Int) {
//        sharedViewModel.setTrainPlanId(id)
//        sharedViewModel.navigateRightTo(R.id.action_showTrainPlan)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}