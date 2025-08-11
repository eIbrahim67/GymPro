package com.eibrahim67.gympro.personalData

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.response.ResponseEI
import com.eibrahim67.gympro.databinding.FragmentPersonalDataBinding
import com.google.android.material.snackbar.Snackbar

class PersonalDataFragment : Fragment() {

    private var _binding: FragmentPersonalDataBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PersonalDataViewModel by viewModels {
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        PersonalDataViewModelFactory(userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadPersonalData()
        viewModel.personalData.observe(viewLifecycleOwner) { data ->
            when (data) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {

                    binding.updateUserName.setText(data.data?.username)
                    binding.updateName.setText(data.data?.name)
                    binding.updateEmail.setText(data.data?.email)
                    binding.updatePhone.setText(data.data?.phone)
                    binding.updateBody.setText(data.data?.typeBody)
                    binding.updateTrainPlan.setText(data.data?.trainPlanId.toString())

                }

                is ResponseEI.Failure -> {}
            }
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.updateBtn.setOnClickListener {
            viewModel.updateName(binding.updateName.text?.toString()?.trim())
            viewModel.updatePhone(binding.updatePhone.text?.toString()?.trim())
            viewModel.updateTypeBody(binding.updateBody.text?.toString()?.trim())
        }

        viewModel.updateName.observe(viewLifecycleOwner) { name ->
            when (name) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    Snackbar.make(requireView(), "Information Successfully", Snackbar.LENGTH_SHORT)
                        .show()
                }

                is ResponseEI.Failure -> {
                    Snackbar.make(
                        requireView(), "Something wrong with Name", Snackbar.LENGTH_INDEFINITE
                    ).setAction("Ok") { }.show()
                }
            }
        }

        viewModel.updatePhone.observe(viewLifecycleOwner) { phone ->
            when (phone) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    Snackbar.make(requireView(), "Information Successfully", Snackbar.LENGTH_SHORT)
                        .show()
                }

                is ResponseEI.Failure -> {
                    Snackbar.make(
                        requireView(), "Something wrong with Phone", Snackbar.LENGTH_INDEFINITE
                    ).setAction("Ok") { }.show()
                }
            }
        }

        viewModel.updateTypeBody.observe(viewLifecycleOwner) { typeBody ->
            when (typeBody) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    Snackbar.make(
                        requireView(), "Information Updated Successfully", Snackbar.LENGTH_SHORT
                    ).show()
                }

                is ResponseEI.Failure -> {
                    Snackbar.make(
                        requireView(), "Something wrong with Type-Body", Snackbar.LENGTH_INDEFINITE
                    ).setAction("Ok") { }.show()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
