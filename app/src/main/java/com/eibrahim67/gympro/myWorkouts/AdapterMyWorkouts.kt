package com.eibrahim67.gympro.myWorkouts

import android.app.AlertDialog
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
    private val deleteWorkout: ((id: Int) -> Unit)
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
                AlertDialog.Builder(context)
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        deleteWorkout(item.id)
                        removeWorkoutById(item.id)

                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    fun removeWorkoutById(id: Int) {
        val currentList = differ.currentList.toMutableList()
        val position = currentList.indexOfFirst { it.id == id }
        if (position != -1) {
            currentList.removeAt(position)
            differ.submitList(currentList)
            notifyItemRemoved(position)
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
                oldItem.id == newItem.id   // ✅ compare IDs

            override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean =
                oldItem == newItem
        }
    }


    class CategoryViewHolder(val binding: ItemMyFeaturedBinding) :
        RecyclerView.ViewHolder(binding.root)
}
