package com.example.admin_gosti

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_gosti.adapter.OrderDetailsAdapter
import com.example.admin_gosti.databinding.ActivityOrderDetailsBinding
import com.example.admin_gosti.model.OrderDetails
import com.example.admin_gosti.model.OrderItem
import com.example.admin_gosti.model.OrderStatus
import com.example.admin_gosti.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailsBinding
    private var currentOrder: OrderDetails? = null

    // User/order details
    private var userName: String? = null
    private var address: String? = null
    private var phoneNumber: String? = null
    private var totalPrice: String? = null


    private val foodNames = arrayListOf<String>()
    private val foodImages = arrayListOf<String>()
    private val foodQuantities = arrayListOf<Int>()
    private val foodPrices = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backeButton.setOnClickListener { finish() }

        getDataFromIntent()
        setupDispatchButton()
    }

    private fun getDataFromIntent() {
        currentOrder = intent.getSerializableExtra("UserOrderDetails") as? OrderDetails

        currentOrder?.let { order ->

            // User info
            userName = order.userName ?: "Unknown"
            address = order.address ?: "Unknown"
            phoneNumber = order.phoneNumber ?: "Unknown"
            totalPrice = order.totalPrice.toString()

            foodNames.clear()
            foodImages.clear()
            foodQuantities.clear()
            foodPrices.clear()

            order.items.forEach { item: OrderItem ->

                foodNames.add(item.name ?: "Unknown")
                foodImages.add(item.image ?: "")

                foodQuantities.add(item.quantity ?: 0)

                foodPrices.add(item.price?.toString() ?: "0")
            }

            setUserDetail()
            setAdapter()
        }
    }

    private fun setUserDetail() {
        binding.name.text = userName
        binding.address.text = address
        binding.phone.text = phoneNumber
        binding.totalPay.text = totalPrice
    }

    private fun setAdapter() {
        binding.orderDetailRecyclerVew.layoutManager = LinearLayoutManager(this)
        binding.orderDetailRecyclerVew.adapter = OrderDetailsAdapter(
            this,
            foodNames,
            foodImages,
            foodQuantities,
            foodPrices
        )
    }

    /**
     * ✅ Dispatch allowed ONLY when status = PENDING_ACCEPTED
     */
    private fun setupDispatchButton() {
        currentOrder?.let { order ->

            val canDispatch = order.orderStatus == OrderStatus.PENDING_ACCEPTED

            binding.dispatchButton.isEnabled = canDispatch
            binding.dispatchButton.text =
                if (canDispatch) "Dispatch" else "Dispatched"

            binding.dispatchButton.setOnClickListener {
                if (canDispatch) {
                    dispatchOrder(order)
                } else {
                    Toast.makeText(
                        this,
                        "Order already dispatched",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun dispatchOrder(order: OrderDetails) {
        val orderId = order.id ?: return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiService.dispatchOrder(orderId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        Toast.makeText(
                            this@OrderDetailsActivity,
                            "Order dispatched successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        // ✅ Update local state
                        order.orderStatus = OrderStatus.DISPATCHED
                        binding.dispatchButton.text = "Dispatched"
                        binding.dispatchButton.isEnabled = false

                        // ✅ Go back to Pending list (item removed)
                        finish()

                    } else {
                        Toast.makeText(
                            this@OrderDetailsActivity,
                            "Dispatch failed: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@OrderDetailsActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
