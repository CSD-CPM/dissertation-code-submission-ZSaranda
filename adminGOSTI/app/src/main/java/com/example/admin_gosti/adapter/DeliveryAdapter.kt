package com.example.admin_gosti.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_gosti.databinding.DeliveryItemBinding
import com.example.admin_gosti.model.OrderDetails
import com.example.admin_gosti.model.OrderStatus

class DeliveryAdapter(
    private val orders: MutableList<OrderDetails>,
    private val listener: Listener
) : RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

    interface Listener {
        fun onPaymentClick(order: OrderDetails)
        fun onCompleteClick(order: OrderDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding = DeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    fun updateData(newOrders: List<OrderDetails>) {
        orders.clear()
        orders.addAll(newOrders)
        notifyDataSetChanged()
    }

    inner class DeliveryViewHolder(private val binding: DeliveryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: OrderDetails) {
            binding.customerName.text = order.userName ?: "Unknown"

            // Update UI for payment and completion buttons
            updatePaymentUI(order.paymentReceived)
            updateCompleteUI(order.orderStatus, order.paymentReceived)

            // Payment button click
            binding.statusMoney.setOnClickListener {
                if (!order.paymentReceived && order.orderStatus == OrderStatus.DISPATCHED) {
                    listener.onPaymentClick(order)
                }
            }

            // Complete button click
            binding.statusColor.setOnClickListener {
                if (order.orderStatus == OrderStatus.DISPATCHED && order.paymentReceived) {
                    listener.onCompleteClick(order)
                }
            }
        }

        private fun updatePaymentUI(isPaid: Boolean) {
            val color = if (isPaid) Color.GREEN else Color.RED
            binding.statusMoney.text = if (isPaid) "Received" else "Not Received"
            binding.statusMoney.setTextColor(color)
        }

        private fun updateCompleteUI(status: OrderStatus, paymentReceived: Boolean) {
            val color = if (status == OrderStatus.DISPATCHED && paymentReceived) Color.GREEN else Color.RED
            binding.statusColor.backgroundTintList = ColorStateList.valueOf(color)
        }
    }
}
