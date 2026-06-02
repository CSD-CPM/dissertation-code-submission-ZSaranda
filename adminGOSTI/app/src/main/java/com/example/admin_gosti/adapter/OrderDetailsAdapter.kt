package com.example.admin_gosti.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.admin_gosti.databinding.OrderDetailItemBinding

class OrderDetailsAdapter(
    private val context: Context,
    private val foodNames: List<String>,
    private val foodImages: List<String>,
    private val foodQuantities: List<Int>,
    private val foodPrices: List<String>
) : RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val binding = OrderDetailItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = foodNames.size

    inner class OrderDetailsViewHolder(private val binding: OrderDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.apply {
                foodName.text = foodNames[position]
                foodQuantity.text = foodQuantities.getOrNull(position)?.toString() ?: "0"
                foodPrice.text = foodPrices.getOrNull(position) ?: "0"

                val imageUrl = foodImages.getOrNull(position)
                if (!imageUrl.isNullOrEmpty()) {
                    val uri = Uri.parse(imageUrl)
                    foodImage.load(uri)
                } else {
                    foodImage.setImageResource(android.R.color.transparent) // placeholder
                }
            }
        }
    }
}
