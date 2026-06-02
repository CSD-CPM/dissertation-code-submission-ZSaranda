package com.example.gosti

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gosti.Fragment.CongratsBottomSheet
import com.example.gosti.Model.CartItems
import com.example.gosti.Model.OrderRequestDTO
import com.example.gosti.Network.RetrofitInstance
import com.example.gosti.databinding.ActivityPayOutBinding
import com.example.gosti.session.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PayOutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPayOutBinding
    private lateinit var cartItems: ArrayList<CartItems>
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Disable editing total amount
        binding.totalAmount.apply {
            isEnabled = false
            isFocusable = false
            isClickable = false
        }

        cartItems = intent.getParcelableArrayListExtra("CartItems") ?: arrayListOf()

        binding.totalAmount.setText(calculateTotalAmount().toString())

        UserSession.user?.let {
            binding.name.setText(it.name ?: "")
            binding.address.setText(it.address ?: "")
            binding.phone.setText(it.phone ?: "")
        }

        binding.backeButton.setOnClickListener { finish() }

        binding.PlaceMyOrder.setOnClickListener {
            binding.PlaceMyOrder.isEnabled = false

            val name = binding.name.text.toString().trim()
            val address = binding.address.text.toString().trim()
            val phone = binding.phone.text.toString().trim()

            if (name.isBlank() || address.isBlank() || phone.isBlank()) {
                showToast("Please enter all the details 😜")
                binding.PlaceMyOrder.isEnabled = true
            } else {
                placeOrder(name, address, phone)
            }
        }
    }

    // ================= PLACE ORDER (UPDATED WITH QR SUPPORT) =================
    private fun placeOrder(name: String, address: String, phone: String) {

        val userId = UserSession.user?.id?.toString() ?: return
        val orderTime = System.currentTimeMillis()

        // ================= QR DATA =================
        val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val restaurantId = sharedPref.getString("restaurant_id", "")
        val tableNumber = sharedPref.getString("table_number", "")

        // Build order lists
        val foodNames = ArrayList<String>()
        val foodImages = ArrayList<String>()
        val foodPrices = ArrayList<Int>()
        val foodQuantities = ArrayList<Int>()

        cartItems.forEach { item ->
            foodNames.add(item.foodName)
            foodImages.add(item.foodImage)

            val priceInt = item.foodPrice.toDoubleOrNull()?.toInt() ?: 0
            foodPrices.add(priceInt)

            foodQuantities.add(item.foodQuantity)
        }

        // ================= ORDER REQUEST =================
        val orderRequest = OrderRequestDTO(
            userUid = userId,
            userName = name,
            foodNames = foodNames,
            foodImages = foodImages,
            foodPrices = foodPrices,
            foodQuantities = foodQuantities,
            address = address,
            totalPrice = calculateTotalAmount(),
            phoneNumber = phone,
            itemPushKey = UUID.randomUUID().toString(),
            orderTime = orderTime,

            // 🔥 QR TABLE SUPPORT ADDED
            restaurantId = restaurantId,
            tableNumber = tableNumber
        )

        RetrofitInstance.orderApi.placeOrder(orderRequest)
            .enqueue(object : Callback<Void> {

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        showToast("Order placed successfully 😁")
                        removeItemsFromCart()
                        showCongratsBottomSheet()
                    } else {
                        showToast("Failed to place order 😒 (${response.code()})")
                        binding.PlaceMyOrder.isEnabled = true
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    showToast("Error: ${t.localizedMessage}")
                    binding.PlaceMyOrder.isEnabled = true
                }
            })
    }

    // ================= REMOVE CART ITEMS =================
    private fun removeItemsFromCart() {
        cartItems.forEach { item ->
            item.id?.let {
                RetrofitInstance.cartApi.deleteCartItem(it)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {}
                        override fun onFailure(call: Call<Void>, t: Throwable) {}
                    })
            }
        }
    }

    // ================= SUCCESS UI =================
    private fun showCongratsBottomSheet() {
        CongratsBottomSheet().show(supportFragmentManager, "Congrats")
        finish()
    }

    // ================= TOTAL CALCULATION =================
    private fun calculateTotalAmount(): Int {
        var total = 0
        cartItems.forEach { item ->
            val price = item.foodPrice.toDoubleOrNull()?.toInt() ?: 0
            total += price * item.foodQuantity
        }
        return total
    }


    private fun showToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast?.show()
    }
}