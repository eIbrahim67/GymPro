package com.eibrahim67.gympro.myTrainingPlans

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.eibrahim67.gympro.databinding.ItemMyFeaturedPlansBinding

class AdapterRVMyFeaturedPlans(
    private val goToTrainPlan: ((id: Int) -> Unit)
) : RecyclerView.Adapter<AdapterRVMyFeaturedPlans.CategoryViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemMyFeaturedPlansBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = differ.currentList[position]

        with(holder.binding) {
            Glide.with(root.context)
                .load(item.imageUrl)
                .centerCrop()
                .into(itemImageFeaturePlan)

            itemTitleFeaturePlan.text = item.name
            itemDurationFeaturePlan.text = "${item.durationDaysPerTrainingWeek} Day per training week"

            itemSeeDetailsFeaturePlan.setOnClickListener {
                goToTrainPlan(item.id)
            }

            deletePlan.setOnClickListener {

            }
        }
    }

    private val differ: AsyncListDiffer<TrainPlan> =
        AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(articleList: List<TrainPlan>) {
        differ.submitList(articleList)
    }

    override fun getItemCount(): Int = differ.currentList.size

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TrainPlan>() {
            override fun areItemsTheSame(oldItem: TrainPlan, newItem: TrainPlan): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(oldItem: TrainPlan, newItem: TrainPlan): Boolean =
                oldItem == newItem
        }
    }

    class CategoryViewHolder(val binding: ItemMyFeaturedPlansBinding) :
        RecyclerView.ViewHolder(binding.root)
}
