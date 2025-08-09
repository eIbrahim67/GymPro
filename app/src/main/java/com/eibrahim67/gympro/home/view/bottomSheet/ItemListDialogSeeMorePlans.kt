package com.eibrahim67.gympro.home.view.bottomSheet

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSourceImpl
import com.eibrahim67.gympro.core.data.response.ResponseEI
import com.eibrahim67.gympro.databinding.FragmentItemListDialogSeeMorePlansListDialogItemBinding
import com.eibrahim67.gympro.databinding.FragmentItemListDialogSeeMorePlansListDialogBinding
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.main.viewModel.MainViewModelFactory
import com.eibrahim67.gympro.train.viewModel.TrainViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.getValue


class ItemListDialogSeeMorePlans(
    private val goToTrainPlan: ((id: Int) -> Unit)
) : BottomSheetDialogFragment() {

    private var _binding: FragmentItemListDialogSeeMorePlansListDialogBinding? = null
    private val binding get() = _binding!!

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
    ): View? {

        _binding =
            FragmentItemListDialogSeeMorePlansListDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.list.layoutManager = LinearLayoutManager(context)

        sharedViewModel.getTrainPlans()
        sharedViewModel.trainPlans.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Loading -> {}
                is ResponseEI.Success -> {
                    binding.list.adapter =
                        ItemAdapter(response.data) { id -> goToTrainPlanWithDismiss(id) }
                }

                is ResponseEI.Failure -> {}
            }
        }
    }

    fun goToTrainPlanWithDismiss(id: Int) {
        goToTrainPlan(id)
        dismiss()
    }

    private inner class ViewHolder internal constructor(binding: FragmentItemListDialogSeeMorePlansListDialogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val itemTitleFeaturePlan = binding.itemTitleFeaturePlan
        val itemImageFeaturePlan = binding.itemImageFeaturePlan
        val itemSeeDetailsFeaturePlan = binding.itemSeeDetailsFeaturePlan
        val itemDurationFeaturePlan = binding.itemDurationFeaturePlan
    }

    private inner class ItemAdapter(
        private val list: List<TrainPlan>, private val goToTrainPlan: ((id: Int) -> Unit)
    ) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            return ViewHolder(
                FragmentItemListDialogSeeMorePlansListDialogItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemDurationFeaturePlan.text =
                "${list[position].avgTimeMinPerWorkout} Day per training week"

            Glide.with(holder.itemView.context).load(list[position]).centerCrop()
                .into(holder.itemImageFeaturePlan)

            holder.itemTitleFeaturePlan.text = list[position].name

            holder.itemSeeDetailsFeaturePlan.setOnClickListener { goToTrainPlan(list[position].id) }
        }

        override fun getItemCount(): Int {
            return list.size
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}