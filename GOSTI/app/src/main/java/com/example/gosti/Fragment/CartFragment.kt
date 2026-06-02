package com.example.gosti.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gosti.Adapter.CartAdapter
import com.example.gosti.Model.CartItems
import com.example.gosti.PayOutActivity
import com.example.gosti.Network.RetrofitInstance
import com.example.gosti.databinding.FragmentCartBinding
import com.example.gosti.session.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private var cartItems: MutableList<CartItems> = mutableListOf()
    private lateinit var adapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        setupRecyclerView()
        fetchCartItems()

        binding.proceedButton.setOnClickListener {
            goToPayment()
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = CartAdapter(requireContext(), cartItems,
            onUpdate = { item -> updateCartItem(item) },
            onDelete = { item -> deleteCartItem(item) })
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = adapter
    }

    private fun fetchCartItems() {
        val user = UserSession.user ?: return
        val userId = user.id.toString()

        RetrofitInstance.cartApi.getCartItems(userId)
            .enqueue(object : Callback<List<CartItems>> {
                override fun onResponse(call: Call<List<CartItems>>, response: Response<List<CartItems>>) {
                    if (response.isSuccessful) {
                        cartItems.clear()
                        response.body()?.let { cartItems.addAll(it) }
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireContext(), "Failed to fetch cart", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<CartItems>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun updateCartItem(item: CartItems) {
        val itemId = item.id ?: return
        RetrofitInstance.cartApi.updateCartItem(itemId, item)
            .enqueue(object : Callback<CartItems> {
                override fun onResponse(call: Call<CartItems>, response: Response<CartItems>) {
                    if (response.isSuccessful) Toast.makeText(requireContext(), "Item updated", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<CartItems>, t: Throwable) {
                    Toast.makeText(requireContext(), "Update failed: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun deleteCartItem(item: CartItems) {
        val itemId = item.id ?: return
        RetrofitInstance.cartApi.deleteCartItem(itemId)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        cartItems.remove(item)
                        adapter.notifyDataSetChanged()
                        Toast.makeText(requireContext(), "Item removed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(requireContext(), "Delete failed: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun goToPayment() {
        if (cartItems.isEmpty()) {
            Toast.makeText(requireContext(), "Cart is empty 😒", Toast.LENGTH_SHORT).show()
            return
        }

        val foodNames = ArrayList(cartItems.map { it.foodName ?: "" })
        val foodPrices = ArrayList(cartItems.map { it.foodPrice ?: "" })
        val foodImages = ArrayList(cartItems.map { it.foodImage ?: "" })
        val foodQuantities = ArrayList(cartItems.map { it.foodQuantity ?: 1 })

        val intent = Intent(requireContext(), PayOutActivity::class.java).apply {
            putStringArrayListExtra("FoodItemName", foodNames)
            putStringArrayListExtra("FoodItemPrice", foodPrices)
            putStringArrayListExtra("FoodItemImage", foodImages)
            putIntegerArrayListExtra("FoodItemQuantities", foodQuantities)
            putParcelableArrayListExtra("CartItems", ArrayList(cartItems))
        }
        startActivity(intent)
    }
}
