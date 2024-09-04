package com.eibrahim67.gympro.train.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.writtenData.model.Exercise
import com.google.android.material.card.MaterialCardView

class AdapterRVExercises(
    private val goToExercise: ((id: Int) -> Unit)
) :
    RecyclerView.Adapter<AdapterRVExercises.CategoryViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_workout_exercise, parent, false)
        parent.context
        return CategoryViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        holder.exerciseNum.text = (position + 1).toString()

        differ.currentList[position].name.let { data ->
            holder.exerciseTitle.text = data
        }

        differ.currentList[position].exerciseHint.let { data ->
            holder.exerciseHint.text = data
        }

        differ.currentList[position].exerciseSet.let { data ->
            holder.exerciseIntensity.text = data.toString() + " %"
        }

        differ.currentList[position].exerciseReps.let { data ->
            holder.exerciseReps.text = data.toString() + " Reps"
        }

        differ.currentList[position].exerciseSet.let { data ->
            holder.exerciseSet.text = data.toString() + " Sets"
        }

        holder.exerciseOpenBtn.setOnClickListener {
            goToExercise(differ.currentList[position].id)
        }

    }

    private val differ: AsyncListDiffer<Exercise> =
        AsyncListDiffer(this, DIFF_CALLBACK)


    fun submitList(articleList: List<Exercise>) {
        differ.submitList(articleList)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Exercise>() {
            override fun areItemsTheSame(
                oldItem: Exercise,
                newItem: Exercise
            ): Boolean =
                oldItem === newItem // this is data class

            override fun areContentsTheSame(
                oldItem: Exercise,
                newItem: Exercise
            ): Boolean = oldItem == newItem
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseNum: TextView = itemView.findViewById(R.id.itemExerciseNum)
        val exerciseTitle: TextView = itemView.findViewById(R.id.itemExerciseTitle)
        val exerciseHint: TextView = itemView.findViewById(R.id.itemExerciseHint)
        val exerciseIntensity: TextView = itemView.findViewById(R.id.itemExerciseIntensity)
        val exerciseSet: TextView = itemView.findViewById(R.id.itemExerciseSet)
        val exerciseReps: TextView = itemView.findViewById(R.id.itemExerciseReps)
        val exerciseOpenBtn: MaterialCardView = itemView.findViewById(R.id.itemExerciseOpenBtn)

    }
}
