package com.example.admin_gosti

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_gosti.adapter.MenuItemAdapter
import com.example.admin_gosti.databinding.ActivityAllItemBinding
import com.example.admin_gosti.model.AllMenu
import com.example.admin_gosti.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class AllItemActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAllItemBinding.inflate(layoutInflater) }

    private val menuApi by lazy { ApiClient.menuApi }

    private val menuItems = arrayListOf<AllMenu>()
    private lateinit var adapter: MenuItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }

        setupRecyclerView()
        fetchMenuItems()
    }

    private fun setupRecyclerView() {
        adapter = MenuItemAdapter(this, menuItems) { position ->
            deleteMenuItem(position)
        }

        binding.MenuRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.MenuRecyclerView.adapter = adapter
    }

    private fun fetchMenuItems() {
        lifecycleScope.launch {
            try {
                val response = menuApi.getAllMenuItems()

                if (response.isSuccessful) {
                    val list = response.body().orEmpty()

                    menuItems.clear()
                    menuItems.addAll(list)

                    adapter.resetQuantities()
                    adapter.notifyDataSetChanged()
                } else {
                    showToast("Server error: ${response.code()}")
                }

            } catch (e: Exception) {
                showToast("Failed: ${e.localizedMessage}")
            }
        }
    }

    private fun deleteMenuItem(position: Int) {
        val item = menuItems.getOrNull(position) ?: return
        val id = item.id ?: return showToast("Missing ID")

        lifecycleScope.launch {
            try {
                val response = menuApi.deleteMenuItem(id)

                if (response.isSuccessful) {
                    menuItems.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    showToast("Deleted successfully")
                } else {
                    showToast("Delete failed: ${response.code()}")
                }

            } catch (e: Exception) {
                showToast("Error: ${e.localizedMessage}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}