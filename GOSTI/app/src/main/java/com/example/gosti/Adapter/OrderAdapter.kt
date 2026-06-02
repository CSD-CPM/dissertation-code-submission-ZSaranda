package com.example.gosti.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gosti.Model.OrderDetails
import com.example.gosti.databinding.OrderItemLayoutBinding
import java.text.SimpleDateFormat
import java.util.*

class OrderAdapter(
    private val context: Context,
    private val orderList: List<OrderDetails>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = OrderItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    override fun getItemCount(): Int = orderList.size

    inner class OrderViewHolder(
        private val binding: OrderItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: OrderDetails) {
            // Remove normalize() call — it's not needed

            // Format order date
            val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            val date = if (order.orderTime > 0) {
                dateFormat.format(Date(order.orderTime))
            } else {
                "Unknown"
            }

            binding.orderDate.text = date
            binding.orderTotal.text = "Total: € %.2f".format(order.totalPrice ?: 0.0)

            // Clear previous item views if reused
            binding.itemContainer.removeAllViews()

            // Dynamically add item name and quantity from order.items
            order.items.forEach { item ->
                val itemLayout = LinearLayout(context).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    setPadding(0, 4, 0, 4)
                }

                val nameTextView = TextView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2f
                    )
                    text = item.name
                    setTextColor(context.getColor(com.example.gosti.R.color.textColor1))
                    textSize = 16f
                }

                val quantityTextView = TextView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    text = "x${item.quantity}"
                    setTextColor(context.getColor(com.example.gosti.R.color.textColor1))
                    textSize = 16f
                }

                itemLayout.addView(nameTextView)
                itemLayout.addView(quantityTextView)

                binding.itemContainer.addView(itemLayout)
            }
        }
    }
}
