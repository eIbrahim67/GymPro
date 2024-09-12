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
import com.eibrahim67.gympro.core.data.remote.model.TrainPlan
import com.google.android.material.card.MaterialCardView

class AdapterRVFeaturedPlans(
    private val goToTrainPlan: ((id: Int) -> Unit)
) :
    RecyclerView.Adapter<AdapterRVFeaturedPlans.CategoryViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_featured_plans, parent, false)
        parent.context
        return CategoryViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        differ.currentList[position].imageUrl?.let { url ->
            Glide
                .with(context)
                .load(url)
                .centerCrop()
                //.placeholder(R.drawable.placeholder_image_svg)
                .into(holder.imageFeaturePlan)
            //itemView.setOnClickListener { goToSearch?.let { it(category.strCategory) } }
        }

        differ.currentList[position].name.let { title ->
            holder.titleFeaturePlan.text = title
        }

        differ.currentList[position].durationDaysPerTrainingWeek.let { data ->
            holder.infoFeaturePlan.text = "$data Day per training week"
        }

        holder.seeDetailsFeaturePlan.setOnClickListener {
            goToTrainPlan(differ.currentList[position].id)
        }

    }

    private val differ: AsyncListDiffer<TrainPlan> =
        AsyncListDiffer(this, DIFF_CALLBACK)


    fun submitList(articleList: List<TrainPlan>) {
        differ.submitList(articleList)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TrainPlan>() {
            override fun areItemsTheSame(
                oldItem: TrainPlan,
                newItem: TrainPlan
            ): Boolean =
                oldItem === newItem // this is data class

            override fun areContentsTheSame(
                oldItem: TrainPlan,
                newItem: TrainPlan
            ): Boolean = oldItem == newItem
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageFeaturePlan: ImageView = itemView.findViewById(R.id.itemImageFeaturePlan)
        val titleFeaturePlan: TextView = itemView.findViewById(R.id.itemTitleFeaturePlan)
        val infoFeaturePlan: TextView = itemView.findViewById(R.id.itemDurationFeaturePlan)
        val seeDetailsFeaturePlan: MaterialCardView =
            itemView.findViewById(R.id.itemSeeDetailsFeaturePlan)

    }
}
