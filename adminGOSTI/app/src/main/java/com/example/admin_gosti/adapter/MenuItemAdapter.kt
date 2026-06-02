package com.example.admin_gosti.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.admin_gosti.R
import com.example.admin_gosti.databinding.ItemItemBinding
import com.example.admin_gosti.model.AllMenu

class MenuItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    private val onDeleteClickListener: (position: Int) -> Unit
) : RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>() {

    private val BASE_IMAGE_URL = "http://192.168.1.19:8080/uploads/"

    private val itemQuantities = mutableListOf<Int>()

    init {
        resetQuantities()
    }

    fun resetQuantities() {
        itemQuantities.clear()
        repeat(menuList.size) { itemQuantities.add(1) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuList.size

    inner class AddItemViewHolder(
        private val binding: ItemItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {

            val menuItem = menuList[position]
            val quantity = itemQuantities[position]

            binding.apply {

                // TEXT
                foodNameTextView.text = menuItem.foodName ?: ""
                priceTextView.text = menuItem.foodPrice ?: ""

                // FIXED TYPO HERE
                quantityTextVIew.text = quantity.toString()

                // IMAGE
                val image = menuItem.foodImage ?: ""

                val finalImageUrl = if (image.startsWith("http")) {
                    image.replace("http:/", "http://")
                } else {
                    BASE_IMAGE_URL + image
                }

                foodImageView.load(finalImageUrl) {
                    placeholder(R.drawable.placeholder)
                    error(R.drawable.placeholder)
                }

                // BUTTONS
                pluseButton.setOnClickListener { increaseQuantity(position) }
                minusButton.setOnClickListener { decreaseQuantity(position) }
                deleteButton.setOnClickListener { onDeleteClickListener(position) }
            }
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                notifyItemChanged(position)
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                notifyItemChanged(position)
            }
        }
    }
}