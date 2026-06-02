package com.example.gosti.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gosti.R
import com.example.gosti.databinding.RecentBuyItemBinding

class RecentBuyAdapter(
    private val context: Context,
    private val foodNameList: ArrayList<String>,
    private val foodPriceList: ArrayList<String>,
    private val foodQuantityList: ArrayList<Int>,
    private val foodImageList: ArrayList<String>
) : RecyclerView.Adapter<RecentBuyAdapter.RecentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val binding = RecentBuyItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecentViewHolder(binding)
    }

    override fun getItemCount(): Int = foodNameList.size

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class RecentViewHolder(private val binding: RecentBuyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.apply {
                // Use the correct IDs from your XML
                buyAgainFoodName.text = foodNameList[position]
                buyAgainFoodPrice.text = foodPriceList[position]




            }
        }
    }
}
