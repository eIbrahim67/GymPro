package com.eibrahim67.gympro.myWorkouts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.core.data.remote.model.Workout
import com.eibrahim67.gympro.databinding.ItemMyFeaturedBinding

class AdapterMyWorkouts(
    private val goToWorkout: ((id: Int) -> Unit),
    private val deleteTrainPlan: ((id: Int) -> Unit)
) : RecyclerView.Adapter<AdapterMyWorkouts.CategoryViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemMyFeaturedBinding.inflate(
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
                .into(itemFeatureImage)

            itemFeatureName.text = item.name
            itemFeatureDescription.text = item.description

            itemFeatureSeeDetails.setOnClickListener {
                goToWorkout(item.id)
            }

            itemFeatureDelete.setOnClickListener {
                deleteTrainPlan(item.id)
            }
        }
    }

    private val differ: AsyncListDiffer<Workout> =
        AsyncListDiffer(this, DIFF_CALLBACK)
    fun submitList(articleList: List<Workout>) {
        differ.submitList(articleList)
    }

    override fun getItemCount(): Int = differ.currentList.size

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Workout>() {
            override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean =
                oldItem == newItem
        }
    }

    class CategoryViewHolder(val binding: ItemMyFeaturedBinding) :
        RecyclerView.ViewHolder(binding.root)
}
