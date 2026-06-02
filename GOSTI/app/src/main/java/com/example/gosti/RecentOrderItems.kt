package com.example.gosti

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gosti.Adapter.OrderAdapter
import com.example.gosti.Model.OrderDetails
import com.example.gosti.Network.RetrofitInstance
import com.example.gosti.databinding.ActivityRecentOrderItemsBinding
import com.example.gosti.session.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecentOrderItems : AppCompatActivity() {

    private lateinit var binding: ActivityRecentOrderItemsBinding
    private lateinit var orderAdapter: OrderAdapter
    private val orderList: MutableList<OrderDetails> = mutableListOf()

    private val userId: String
        get() = UserSession.user?.id?.toString() ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecentOrderItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }

        setupRecyclerView()
        fetchAllRecentOrders()
    }

    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter(this, orderList)
        binding.recyclerViewOrderItems.apply {
            layoutManager = LinearLayoutManager(this@RecentOrderItems)
            adapter = orderAdapter
            setHasFixedSize(true)
        }
    }

    private fun fetchAllRecentOrders() {
        if (userId.isBlank()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitInstance.orderApi.getUserOrders(userId)
            .enqueue(object : Callback<List<OrderDetails>> {
                override fun onResponse(
                    call: Call<List<OrderDetails>>,
                    response: Response<List<OrderDetails>>
                ) {
                    if (response.isSuccessful) {
                        val orders = response.body()
                        orderList.clear()
                        orders?.let {
                            orderList.addAll(it.reversed()) // latest orders first
                        }
                        orderAdapter.notifyDataSetChanged()

                        if (orderList.isEmpty()) {
                            Toast.makeText(this@RecentOrderItems, "No recent orders found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@RecentOrderItems, "Failed to fetch orders", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<OrderDetails>>, t: Throwable) {
                    Toast.makeText(this@RecentOrderItems, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
