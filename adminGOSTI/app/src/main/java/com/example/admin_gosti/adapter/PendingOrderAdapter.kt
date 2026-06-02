package com.example.admin_gosti.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_gosti.databinding.PendingOrdersItemBinding
import com.example.admin_gosti.model.OrderDetails
import com.example.admin_gosti.model.OrderStatus

class PendingOrderAdapter(
    private val context: Context,
    private val orders: MutableList<OrderDetails>,
    private val listener: OnItemClicked
) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {

    interface OnItemClicked {
        fun onItemClickListener(order: OrderDetails)
        fun onItemAcceptClickListener(order: OrderDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding = PendingOrdersItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    inner class PendingOrderViewHolder(
        private val binding: PendingOrdersItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: OrderDetails) = with(binding) {

            // 1️⃣ Customer Name
            customerName.text = order.userName ?: "Unknown"

            // Total Quantity
            //
            val totalQuantity = order.items?.sumOf { it.quantity.toInt() } ?: 0
            pendingOredarQuantity.text = "Quantity: $totalQuantity"

            // Total Price

            val totalPriceValue = order.items?.sumOf { it.price.toDouble() * it.quantity.toDouble() } ?: 0.0
            totalPrice.text = String.format("%.2f€", totalPriceValue)

            // 4Accept Button State
            when (order.orderStatus) {
                OrderStatus.PENDING -> {
                    orderedAcceptButton.text = "Accept"
                    orderedAcceptButton.isEnabled = true
                    orderedAcceptButton.setBackgroundColor(Color.parseColor("#FF7A00"))
                }
                OrderStatus.PENDING_ACCEPTED -> {
                    orderedAcceptButton.text = "Accepted"
                    orderedAcceptButton.isEnabled = false
                    orderedAcceptButton.setBackgroundColor(Color.parseColor("#1F8A52"))
                }
                else -> {
                    orderedAcceptButton.text = order.orderStatus.name
                    orderedAcceptButton.isEnabled = false
                    orderedAcceptButton.setBackgroundColor(Color.GRAY)
                }
            }

            //  Click Listeners
            orderedAcceptButton.setOnClickListener {
                if (order.orderStatus == OrderStatus.PENDING) {
                    listener.onItemAcceptClickListener(order)
                }
            }

            binding.root.setOnClickListener {
                listener.onItemClickListener(order)
            }
        }
    }
}
