package com.eibrahim67.gympro.showTrainPlan.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.writtenData.model.Workout
import com.google.android.material.card.MaterialCardView

class AdapterRVWorkoutsTrainingPlan(
    private val goToWorkout: (id: Int) -> Unit
) :
    RecyclerView.Adapter<AdapterRVWorkoutsTrainingPlan.CategoryViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_workout, parent, false)
        parent.context
        return CategoryViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        holder.workoutNum.text = (position + 1).toString()

        differ.currentList[position].name.let { data ->
            holder.workoutTitle.text = data
        }

        differ.currentList[position].targetedMusclesAsString.let { data ->
            holder.workoutTargetedMuscle.text = data
        }

        differ.currentList[position].difficultyLevel.let { data ->
            holder.workoutDifficult.text = data
        }

        differ.currentList[position].durationMinutes.let { data ->
            holder.workoutTime.text = "$data min"
        }

        holder.workoutShowBtn.setOnClickListener {

            goToWorkout(differ.currentList[position].id)

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
        val workoutNum: TextView = itemView.findViewById(R.id.itemWorkoutNum)
        val workoutTitle: TextView = itemView.findViewById(R.id.itemWorkoutTitle)
        val workoutTargetedMuscle: TextView = itemView.findViewById(R.id.itemWorkoutTargetedMuscle)
        val workoutDifficult: TextView = itemView.findViewById(R.id.itemWorkoutDifficult)
        val workoutTime: TextView = itemView.findViewById(R.id.itemWorkoutTime)
        val workoutShowBtn: MaterialCardView = itemView.findViewById(R.id.itemWorkoutShowBtn)

    }
}
