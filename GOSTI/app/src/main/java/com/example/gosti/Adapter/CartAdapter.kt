package com.example.gosti.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gosti.Model.CartItems
import com.example.gosti.R
import com.example.gosti.databinding.CartItemBinding

class CartAdapter(
    private val context: Context,
    private val cartItems: MutableList<CartItems>,
    private val onUpdate: (CartItems) -> Unit,
    private val onDelete: (CartItems) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size

    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItems) = with(binding) {

            cartFoodName.text = item.foodName
            cartItemPrice.text = "€ ${item.foodPrice.toString()}"
            catItemQuantity.text = item.foodQuantity.toString()

            cartImage.load(item.foodImage) {
                placeholder(R.drawable.placeholder)
                error(R.drawable.placeholder)
            }

            plusebutton.setOnClickListener {
                item.foodQuantity += 1
                catItemQuantity.text = item.foodQuantity.toString()
                onUpdate(item)
            }

            minusbutton.setOnClickListener {
                if (item.foodQuantity > 1) {
                    item.foodQuantity -= 1
                    catItemQuantity.text = item.foodQuantity.toString()
                    onUpdate(item)
                }
            }

            deleteButton.setOnClickListener {
                onDelete(item)
            }
        }
    }

    fun updateList(newItems: List<CartItems>) {
        cartItems.clear()
        cartItems.addAll(newItems)
        notifyDataSetChanged()
    }
}
