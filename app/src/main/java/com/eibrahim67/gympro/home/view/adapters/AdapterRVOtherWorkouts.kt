package com.eibrahim67.gympro.home.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.remote.model.Workout

class AdapterRVOtherWorkouts(
    private val goToSearch: ((id: String) -> Unit)? = null
) :
    RecyclerView.Adapter<AdapterRVOtherWorkouts.CategoryViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_others_workouts, parent, false)
        parent.context
        return CategoryViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        differ.currentList[position].imageUrl.let { url ->
            Glide
                .with(context)
                .load(url)
                .centerCrop()
                .into(holder.imageOthersWorkout)
        }

        differ.currentList[position].name.let { title ->
            holder.titleOthersWorkout.text = title
        }

        differ.currentList[position].durationMinutes.let { durationMinutes ->
            holder.avgTimeOthersWorkout.text = "$durationMinutes min"
        }

        differ.currentList[position].equipment.let { title ->
            holder.equipmentOthersWorkout.text = title
        }


    }

    private val differ: AsyncListDiffer<Workout> =
        AsyncListDiffer(this, DIFF_CALLBACK)


    fun submitList(articleList: List<Workout>) {
        differ.submitList(articleList)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Workout>() {
            override fun areItemsTheSame(
                oldItem: Workout,
                newItem: Workout
            ): Boolean =
                oldItem === newItem // this is data class

            override fun areContentsTheSame(
                oldItem: Workout,
                newItem: Workout
            ): Boolean = oldItem == newItem
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageOthersWorkout: ImageView = itemView.findViewById(R.id.itemImageOthersWorkout)
        val titleOthersWorkout: TextView = itemView.findViewById(R.id.itemTitleOthersWorkout)
        val equipmentOthersWorkout: TextView =
            itemView.findViewById(R.id.itemEquipmentOthersWorkout)
        val avgTimeOthersWorkout: TextView =
            itemView.findViewById(R.id.itemAvgTimeOthersWorkout)

    }
}
