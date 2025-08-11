package com.eibrahim67.gympro.myTrainingPlans

import android.os.Bundle
import android.util.Log
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
import com.eibrahim67.gympro.core.response.ResponseEI
import com.eibrahim67.gympro.databinding.FragmentMyTrainingPlansBinding
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.main.viewModel.MainViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

class MyTrainingPlansFragment : Fragment() {

    private var _binding: FragmentMyTrainingPlansBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: MainViewModel by activityViewModels {
        val remoteRepository =
            RemoteRepositoryImpl(RemoteDataSourceImpl(FirebaseFirestore.getInstance()))
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        MainViewModelFactory(userRepository, remoteRepository)
    }


    private val viewModel: MyTrainingPlansViewModel by viewModels {
        val remoteRepository =
            RemoteRepositoryImpl(RemoteDataSourceImpl(FirebaseFirestore.getInstance()))

        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        MyTrainingPlansViewModelFactory(remoteRepository, userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyTrainingPlansBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addNewPlan.setOnClickListener {
            sharedViewModel.navigateRightTo(R.id.action_createPlanFragment)
        }
        val adapterRVMyFeaturedPlans = AdapterRVMyFeaturedPlans { id -> goToTrainPlan(id) }
        binding.recyclerviewWorkoutsTrainPlans.adapter = adapterRVMyFeaturedPlans

        viewModel.getLoggedInUser()
        viewModel.loggedInUser.observe(viewLifecycleOwner) { user ->

            when (user) {
                is ResponseEI.Loading -> {

                }

                is ResponseEI.Success -> {

                    user.data?.let { viewModel.getMyTrainPlansIds(it.id) }

                }

                is ResponseEI.Failure -> {

                }
            }

        }

        viewModel.myTrainPlansIds.observe(viewLifecycleOwner) { myTrainPlansIds ->

            when (myTrainPlansIds) {
                is ResponseEI.Loading -> {
                }

                is ResponseEI.Success -> {


                    myTrainPlansIds.data?.let {
                        viewModel.getTrainPlans(it)
                    }

                }

                is ResponseEI.Failure -> {}
            }

        }

        viewModel.trainPlans.observe(viewLifecycleOwner) { myTrainPlans ->

            when (myTrainPlans) {
                is ResponseEI.Loading -> {
                }

                is ResponseEI.Success -> {
                    myTrainPlans.data?.let { adapterRVMyFeaturedPlans.submitList(myTrainPlans.data) }

                }

                is ResponseEI.Failure -> {
                }
            }

        }


    }

    private fun goToTrainPlan(id: Int) {
        sharedViewModel.setTrainPlanId(id)
        sharedViewModel.navigateRightTo(R.id.action_showTrainPlan)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
