package com.example.admin_gosti

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.admin_gosti.databinding.ActivityMainBinding
import com.example.admin_gosti.model.OrderDetails
import com.example.admin_gosti.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // ✅ FIXED: use apiService (NOT api)
    private val apiService by lazy { ApiClient.apiService }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupNavigation()
        fetchPendingOrders()
        fetchCompletedOrders()
    }

    private fun setupNavigation() {
        binding.addMenu.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }

        binding.allItemMenu.setOnClickListener {
            startActivity(Intent(this, AllItemActivity::class.java))
        }

        binding.outForDeliveryButton.setOnClickListener {
            startActivity(Intent(this, OutForDeliveryActivity::class.java))
        }

        binding.profile.setOnClickListener {
            startActivity(Intent(this, AdminProfileActivity::class.java))
        }

        binding.createUser.setOnClickListener {
            startActivity(Intent(this, CreateUserActivity::class.java))
        }

        binding.pendingOrderTextView.setOnClickListener {
            startActivity(Intent(this, PendingOrderActivity::class.java))
        }

        binding.logoutButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun fetchPendingOrders() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getPendingOrders()
                }

                Log.d("DEBUG", "Response code: ${response.code()}")
                Log.d("DEBUG", "Body: ${response.body()}")

                if (response.isSuccessful) {

                    val pendingOrders = response.body().orEmpty()

                    Log.d("DEBUG", "Pending size = ${pendingOrders.size}")

                    binding.pendingOrders.text = pendingOrders.size.toString()

                } else {
                    showToast("Failed: ${response.code()}")
                }

            } catch (e: Exception) {
                Log.e("DEBUG", "Error fetching pending", e)
                showToast("Error: ${e.localizedMessage}")
            }
        }
    }

    private fun fetchCompletedOrders() {
        lifecycleScope.launch {
            try {
                val response: Response<List<OrderDetails>> =
                    withContext(Dispatchers.IO) {
                        apiService.getCompletedOrders()
                    }

                if (response.isSuccessful) {
                    val completedOrders = response.body().orEmpty()

                    val totalEarnings =
                        completedOrders.sumOf { (it.totalPrice ?: 0.0).toLong() }

                    binding.completeOrders.text = completedOrders.size.toString()
                    binding.wholeTimeEarning.text = "$$totalEarnings"

                } else {
                    showToast("Failed: ${response.code()}")
                }

            } catch (e: Exception) {
                showToast("Error: ${e.localizedMessage}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}