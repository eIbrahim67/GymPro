package com.eibrahim67.gympro.showExercise.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.utils.UtilsFunctions.createMaterialAlertDialogBuilderOk

class AdapterRVExercisesResults(

) : RecyclerView.Adapter<AdapterRVExercisesResults.CategoryViewHolder>() {

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

        holder.itemExerciseSet.text = (position + 1).toString()

        differ.currentList[position].let { data ->
            Log.e("sharedViewModel", "${differ.currentList}")

            try {
                val sData: List<String?> = data.split("#")
                holder.itemExerciseWeight.text = "${sData[0] ?: " error "} kg"
                holder.itemExerciseReps.text = sData[1] ?: " error "
            } catch (e: Exception) {
                createMaterialAlertDialogBuilderOk(
                    context, "Error in data", e.message.toString(), "Ok"
                ) {}
            }
        }

    }

    private val differ: AsyncListDiffer<String> = AsyncListDiffer(this, DIFF_CALLBACK)


    fun submitList(articleList: List<String>?) {
        differ.submitList(articleList)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String, newItem: String
            ): Boolean = oldItem === newItem

            override fun areContentsTheSame(
                oldItem: String, newItem: String
            ): Boolean = oldItem == newItem
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemExerciseWeight: TextView = itemView.findViewById(R.id.itemExerciseWeight)
        val itemExerciseReps: TextView = itemView.findViewById(R.id.itemExerciseReps)
        val itemExerciseSet: TextView = itemView.findViewById(R.id.itemExerciseSet)

    }
}
