package com.example.gosti.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.gosti.Adapter.PopularAdapter
import com.example.gosti.Model.MenuItem
import com.example.gosti.Model.OrderDetails
import com.example.gosti.Network.RetrofitInstance
import com.example.gosti.RecentOrderItems
import com.example.gosti.databinding.FragmentHistoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val listOfOrders = arrayListOf<OrderDetails>()
    private lateinit var menuItems: MutableList<MenuItem>

    private val BASE_URL = "http://192.168.1.19:8080/"

    private val userId: String
        get() = com.example.gosti.session.UserSession.user?.id?.toString() ?: "1"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        fetchOrderHistory()
        loadRecommendedItems()

        return binding.root
    }

    // ===================== RECENT ORDER =======================
    private fun fetchOrderHistory() {
        RetrofitInstance.orderApi.getUserOrders(userId)
            .enqueue(object : Callback<List<OrderDetails>> {
                override fun onResponse(
                    call: Call<List<OrderDetails>>,
                    response: Response<List<OrderDetails>>
                ) {
                    if (response.isSuccessful) {
                        listOfOrders.clear()
                        listOfOrders.addAll(response.body()?.reversed() ?: emptyList())
                        setupRecentBuyUI()
                    }
                }

                override fun onFailure(call: Call<List<OrderDetails>>, t: Throwable) {
                    Log.e("HistoryFragment", "Order history error", t)
                }
            })
    }

    private fun setupRecentBuyUI() {
        val order = listOfOrders.firstOrNull() ?: return
        val item = order.items.firstOrNull() ?: return

        

        val name = item.name.takeIf { it.isNotBlank() } ?: "Unnamed"
        val quantity = item.quantity
        val totalPrice = item.price * quantity

        binding.buyAgainFoodName.text = name
        binding.foodQuantity.text = "x$quantity"
        binding.buyAgainFoodPrice.text = "€ %.2f".format(totalPrice)



        binding.cardRecentBuy.setOnClickListener {
            val intent = Intent(requireContext(), RecentOrderItems::class.java)
            intent.putParcelableArrayListExtra("RecentBuyOrderItem", ArrayList(listOfOrders))
            startActivity(intent)
        }
    }


    private fun loadRecommendedItems() {

        menuItems = mutableListOf()

        RetrofitInstance.menuApi.getAllMenuItems()
            .enqueue(object : Callback<List<MenuItem>> {

                override fun onResponse(
                    call: Call<List<MenuItem>>,
                    response: Response<List<MenuItem>>
                ) {
                    if (response.isSuccessful) {

                        response.body()?.let {

                            menuItems.clear()
                            menuItems.addAll(it)

                            val recommendedItems = menuItems.shuffled().take(6)

                            val adapter = PopularAdapter(recommendedItems, requireContext())

                            binding.recommendedRecyclerView.layoutManager =
                                LinearLayoutManager(requireContext())

                            binding.recommendedRecyclerView.adapter = adapter
                        }
                    }
                }

                override fun onFailure(call: Call<List<MenuItem>>, t: Throwable) {
                    Log.e("HistoryFragment", "Menu fetch error", t)
                }
            })
    }
}