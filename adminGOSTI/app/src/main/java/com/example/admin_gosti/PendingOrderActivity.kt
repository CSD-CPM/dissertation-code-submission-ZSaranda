package com.example.admin_gosti

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_gosti.adapter.PendingOrderAdapter
import com.example.admin_gosti.databinding.ActivityPendingOrderBinding
import com.example.admin_gosti.model.OrderDetails
import com.example.admin_gosti.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PendingOrderActivity : AppCompatActivity(),
    PendingOrderAdapter.OnItemClicked {

    private lateinit var binding: ActivityPendingOrderBinding
    private val ordersList: MutableList<OrderDetails> = mutableListOf()
    private lateinit var adapter: PendingOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchPendingOrders()

        binding.backButton.setOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        adapter = PendingOrderAdapter(this, ordersList, this)
        binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.pendingOrderRecyclerView.adapter = adapter
    }

    private fun fetchPendingOrders() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiService.getPendingOrders()
                if (response.isSuccessful && response.body() != null) {
                    withContext(Dispatchers.Main) {
                        ordersList.clear()
                        ordersList.addAll(response.body()!!)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    showToast("Failed to fetch orders: ${response.code()}")
                }
            } catch (e: Exception) {
                showToast("Error fetching orders: ${e.message}")
            }
        }
    }

    // 📄 Open order details
    override fun onItemClickListener(order: OrderDetails) {
        val intent = Intent(this, OrderDetailsActivity::class.java)
        intent.putExtra("UserOrderDetails", order)
        startActivity(intent)
    }

    // ✅ Accept order (backend-driven)
    override fun onItemAcceptClickListener(order: OrderDetails) {
        val orderId = order.id ?: return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiService.acceptOrder(orderId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        showToast("Order accepted")
                        fetchPendingOrders()
                    } else {
                        showToast("Failed to accept order: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                showToast("Error: ${e.message}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
