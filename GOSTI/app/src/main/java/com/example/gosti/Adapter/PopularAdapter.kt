package com.example.gosti.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gosti.DetailsActivity
import com.example.gosti.Model.MenuItem
import com.example.gosti.R
import com.example.gosti.databinding.PopulerItemBinding

class PopularAdapter(
    private val items: List<MenuItem>,
    private val context: Context
) : RecyclerView.Adapter<PopularAdapter.PopulerViewHolder>() {

    private val BASE_URL = "http://192.168.1.19:8080/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopulerViewHolder {
        val binding = PopulerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PopulerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopulerViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            val imageUrl = item.foodImage?.let {
                if (it.startsWith("http")) it
                else "${BASE_URL}uploads/$it"
            }

            val intent = Intent(context, DetailsActivity::class.java).apply {
                putExtra("MenuItemName", item.foodName)
                putExtra("MenuItemPrice", item.foodPrice)
                putExtra("MenuItemImage", imageUrl) // ✅ FIXED HERE
                putExtra("MenuItemDescription", item.foodDescription)
                putExtra("MenuItemIngredient", item.foodIngredient)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    class PopulerViewHolder(
        private val binding: PopulerItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val BASE_URL = "http://192.168.1.19:8080/"

        fun bind(item: MenuItem) {

            binding.foodNamePopuler.text = item.foodName ?: ""
            binding.PricePopuar.text = "€${item.foodPrice ?: "0"}"

            val imageUrl = item.foodImage?.let {
                if (it.startsWith("http")) it
                else "${BASE_URL}uploads/$it"
            }

            binding.foodImage.load(imageUrl) {
                placeholder(R.drawable.placeholder)
                error(R.drawable.placeholder)
                crossfade(true)
            }
        }
    }
}