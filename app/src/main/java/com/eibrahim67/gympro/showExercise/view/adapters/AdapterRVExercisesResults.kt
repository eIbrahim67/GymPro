package com.eibrahim67.gympro.showExercise.view.adapters

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

class AdapterRVExercisesResults(

) :
    RecyclerView.Adapter<AdapterRVExercisesResults.CategoryViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_exercise_set, parent, false)
        parent.context
        return CategoryViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        differWeight.currentList[position].let { data ->
            holder.itemExerciseWeight.text = data
        }

        differReps.currentList[position].let { data ->
            holder.itemExerciseReps.text = data
        }

    }

    private val differWeight: AsyncListDiffer<String> =
        AsyncListDiffer(this, DIFF_CALLBACK)


    fun submitListWeight(articleList: List<String>?) {
        differWeight.submitList(articleList)
    }

    private val differReps: AsyncListDiffer<String> =
        AsyncListDiffer(this, DIFF_CALLBACK)


    fun submitListReps(articleList: List<String>?) {
        differReps.submitList(articleList)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean =
                oldItem === newItem // this is data class

            override fun areContentsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean = oldItem == newItem
        }
    }

    override fun getItemCount(): Int = differWeight.currentList.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemExerciseWeight: TextView = itemView.findViewById(R.id.itemExerciseWeight)
        val itemExerciseReps: TextView = itemView.findViewById(R.id.itemExerciseReps)

    }
}
