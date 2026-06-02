package com.example.gosti.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gosti.DetailsActivity
import com.example.gosti.Model.CartItems
import com.example.gosti.Model.MenuItem
import com.example.gosti.Network.RetrofitInstance
import com.example.gosti.R
import com.example.gosti.session.UserSession
import com.example.gosti.databinding.MenuItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuAdapter(
    private val menuItems: List<MenuItem>,
    private val context: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private val BASE_URL = "http://192.168.1.19:8080/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menuItems[position])
    }

    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewHolder(
        private val binding: MenuItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val BASE_URL = "http://192.168.1.19:8080/"

        fun bind(menuItem: MenuItem) {

            binding.menuFoodName.text = menuItem.foodName ?: ""
            binding.menuPrice.text = menuItem.foodPrice ?: "0.0"

            val imageUrl = menuItem.foodImage?.let {
                if (it.startsWith("http")) it
                else "${BASE_URL}uploads/$it"
            }

            binding.menuImage.load(imageUrl) {
                placeholder(R.drawable.placeholder)
                error(R.drawable.placeholder)
                crossfade(true)
            }


            binding.root.setOnClickListener {
                val intent = Intent(context, DetailsActivity::class.java).apply {
                    putExtra("MenuItemName", menuItem.foodName)
                    putExtra("MenuItemImage", imageUrl) // ✅ FIXED HERE
                    putExtra("MenuItemDescription", menuItem.foodDescription)
                    putExtra("MenuItemIngredients", menuItem.foodIngredient)
                    putExtra("MenuItemPrice", menuItem.foodPrice)
                }
                context.startActivity(intent)
            }

            binding.menuAddToCart.setOnClickListener {
                addToCart(menuItem)
            }
        }
    }

    private fun addToCart(menuItem: MenuItem) {

        val user = UserSession.user
        if (user == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val cartItem = CartItems(
            userUid = user.id.toString(),
            foodName = menuItem.foodName ?: "",
            foodPrice = menuItem.foodPrice ?: "0.0",
            foodDescription = menuItem.foodDescription ?: "",
            foodImage = menuItem.foodImage ?: "",
            foodQuantity = 1,
            foodIngredients = menuItem.foodIngredient ?: ""
        )

        RetrofitInstance.cartApi.addToCart(cartItem)
            .enqueue(object : Callback<CartItems> {

                override fun onResponse(
                    call: Call<CartItems>,
                    response: Response<CartItems>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Added to cart 🛒", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Add failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CartItems>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}