package com.eibrahim67.gympro.home.view.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.core.data.response.ResponseEI
import com.eibrahim67.gympro.databinding.FragmentSeeMoreBottomSheetListDialogBinding
import com.eibrahim67.gympro.databinding.ItemSeeMoreFeaturedPlansBinding
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.train.viewModel.TrainViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView

// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    SeeMoreBottomSheet.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class SeeMoreBottomSheet(private val goToTrainPlan: ((id: Int) -> Unit)) :
    BottomSheetDialogFragment() {

    private var _binding: FragmentSeeMoreBottomSheetListDialogBinding? = null

    private val sharedViewModel: MainViewModel by activityViewModels {
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        TrainViewModelFactory(userRepository)
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSeeMoreBottomSheetListDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.list.layoutManager =
            LinearLayoutManager(context)

        sharedViewModel.getTrainPlans()
        sharedViewModel.trainPlans.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Success -> {
                    binding.list.adapter = ItemAdapter(response.data, goToTrainPlan)
                }

                is ResponseEI.Failure -> {}
                else -> {}
            }
        }

    }

    private inner class ViewHolder (binding: ItemSeeMoreFeaturedPlansBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val itemTitleFeaturePlan = binding.itemTitleFeaturePlan
        val itemImageFeaturePlan = binding.itemImageFeaturePlan
        val itemSeeDetailsFeaturePlan = binding.itemSeeDetailsFeaturePlan
        val itemDurationFeaturePlan = binding.itemDurationFeaturePlan
    }

    private inner class ItemAdapter(
        private val list: List<TrainPlan>,
        private val goToTrainPlan: ((id: Int) -> Unit)
    ) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            return ViewHolder(
                ItemSeeMoreFeaturedPlansBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemDurationFeaturePlan.text =
                "${list[position].avgTimeMinPerWorkout} Day per training week"

            list[position].imageUrl?.let { url ->
                    Glide
                        .with(holder.itemView.context)
                        .load(url)
                        .centerCrop()
                        .into(holder.itemImageFeaturePlan)
                    //.placeholder(R.drawable.placeholder_image_svg)

            }

            holder.itemTitleFeaturePlan.text = list[position].name

            holder.itemSeeDetailsFeaturePlan.setOnClickListener {

                goToTrainPlan(list[position].id)

            }

        }

        override fun getItemCount(): Int = list.size

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}