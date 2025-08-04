package com.eibrahim67.gympro.myTrainingPlans

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
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSourceImpl
import com.eibrahim67.gympro.core.data.response.ResponseEI
import com.eibrahim67.gympro.databinding.FragmentMyTrainingPlansBinding
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.train.viewModel.TrainViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

class MyTrainingPlansFragment : Fragment() {

    private var _binding: FragmentMyTrainingPlansBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: MainViewModel by activityViewModels {
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        TrainViewModelFactory(userRepository)
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

                    user.data?.let { viewModel.getMyTrainPlans(it.id) }

                }

                is ResponseEI.Failure -> {}
            }

        }

        viewModel.myTrainPlans.observe(viewLifecycleOwner) { myTrainPlans ->

            when (myTrainPlans) {
                is ResponseEI.Loading -> {
                }

                is ResponseEI.Success -> {
                    val list = mutableListOf<TrainPlan>()

                    if (!myTrainPlans.data.isNullOrEmpty())
                        for (id in myTrainPlans.data) {
//                            viewModel.getTrainPlanById()
                        }

                }

                is ResponseEI.Failure -> {}
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
