package com.example.admin_gosti

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_gosti.adapter.DeliveryAdapter
import com.example.admin_gosti.databinding.ActivityOutForDeliveryBinding
import com.example.admin_gosti.model.OrderDetails
import com.example.admin_gosti.model.OrderStatus
import com.example.admin_gosti.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OutForDeliveryActivity : AppCompatActivity() {

    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }

    private val orders: MutableList<OrderDetails> = mutableListOf()
    private lateinit var adapter: DeliveryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }

        setupRecyclerView()
        fetchDispatchedOrders()
    }

    private fun setupRecyclerView() {
        adapter = DeliveryAdapter(orders, object : DeliveryAdapter.Listener {

            override fun onPaymentClick(order: OrderDetails) {
                // Call API to mark payment received
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = ApiClient.apiService.markPaymentReceived(order.id!!)
                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                order.paymentReceived = true
                                adapter.notifyDataSetChanged()
                                Toast.makeText(
                                    this@OutForDeliveryActivity,
                                    "Payment received for ${order.userName}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@OutForDeliveryActivity,
                                    "Failed to mark payment: ${response.code()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@OutForDeliveryActivity,
                                "Error: ${e.localizedMessage}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            override fun onCompleteClick(order: OrderDetails) {
                // Call API to complete order
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = ApiClient.apiService.completeOrder(order.id!!)
                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                order.orderStatus = OrderStatus.COMPLETED
                                orders.remove(order)
                                adapter.notifyDataSetChanged()
                                Toast.makeText(
                                    this@OutForDeliveryActivity,
                                    "Order completed for ${order.userName}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@OutForDeliveryActivity,
                                    "Failed to complete order: ${response.code()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@OutForDeliveryActivity,
                                "Error: ${e.localizedMessage}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        })

        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.deliveryRecyclerView.adapter = adapter
    }

    private fun fetchDispatchedOrders() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiService.getDispatchedOrders()
                if (response.isSuccessful) {
                    val fetchedOrders = response.body()?.toMutableList() ?: mutableListOf()
                    fetchedOrders.reverse() // show latest first
                    withContext(Dispatchers.Main) {
                        adapter.updateData(fetchedOrders)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@OutForDeliveryActivity,
                            "Failed to fetch orders: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@OutForDeliveryActivity,
                        "Error: ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
