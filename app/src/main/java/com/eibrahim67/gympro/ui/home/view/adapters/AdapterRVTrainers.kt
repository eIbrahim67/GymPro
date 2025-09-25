package com.eibrahim67.gympro.ui.home.view.adapters

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
import com.eibrahim67.gympro.data.remote.model.Coach
import com.google.android.material.card.MaterialCardView

class AdapterRVTrainers(
    private val chatWithTrainer: ((id: String) -> Unit)
) :
    RecyclerView.Adapter<AdapterRVTrainers.CategoryViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_our_trainer, parent, false)
        parent.context
        return CategoryViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        differ.currentList[position].profileImageUrl?.let { url ->
            Glide
                .with(context)
                .load(url)
                .centerCrop()
                .into(holder.imageTrainer)
        }

        differ.currentList[position].name.let { name ->
            holder.nameTrainer.text = name
        }

        differ.currentList[position].rate.let { rate ->
            holder.rateTrainer.text = rate.toString()
        }

        differ.currentList[position].price.let { price ->
            holder.trainerPrice.text = "$price EG"
        }

        holder.itemBtnStartChat.setOnClickListener {
            chatWithTrainer(differ.currentList[position].id)
        }

    }

    private val differ: AsyncListDiffer<Coach> =
        AsyncListDiffer(this, DIFF_CALLBACK)


    fun submitList(articleList: List<Coach>) {
        differ.submitList(articleList)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Coach>() {
            override fun areItemsTheSame(
                oldItem: Coach,
                newItem: Coach
            ): Boolean =
                oldItem === newItem // this is data class

            override fun areContentsTheSame(
                oldItem: Coach,
                newItem: Coach
            ): Boolean = oldItem == newItem
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageTrainer: ImageView = itemView.findViewById(R.id.itemImageTrainer)
        val nameTrainer: TextView = itemView.findViewById(R.id.itemNameTrainer)
        val rateTrainer: TextView = itemView.findViewById(R.id.itemRateTrainer)
        val textViewTrainer: TextView = itemView.findViewById(R.id.itemTextViewTrainer)
        val trainerPrice: TextView = itemView.findViewById(R.id.itemTrainerPrice)
        val itemBtnStartChat: MaterialCardView = itemView.findViewById(R.id.itemBtnStartChat)

    }
}
