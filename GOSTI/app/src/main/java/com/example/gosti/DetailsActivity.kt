package com.example.gosti

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.gosti.Model.CartItems
import com.example.gosti.Network.RetrofitInstance
import com.example.gosti.databinding.ActivityDetailsBinding
import com.example.gosti.session.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    private var foodName: String? = null
    private var foodImage: String? = null
    private var foodDescriptions: String? = null
    private var foodIngredients: String? = null
    private var foodPrice: String? = null

    private val BASE_IMAGE_URL = "http://192.168.1.19:8080/uploads/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ================= GET INTENT DATA =================
        foodName = intent.getStringExtra("MenuItemName")
        foodDescriptions = intent.getStringExtra("MenuItemDescription")
        foodIngredients = intent.getStringExtra("MenuItemIngredients")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodImage = intent.getStringExtra("MenuItemImage")

        // ================= UI =================
        binding.apply {

            detailFoodName.text = foodName ?: ""
            detailDescription.text = foodDescriptions ?: ""
            detailIngredients.text = foodIngredients ?: ""

            // ================= IMAGE FIX (ROBUST) =================
            val image = foodImage ?: ""

            val finalImageUrl = when {
                image.startsWith("http://") || image.startsWith("https://") -> {
                    image.replace("http:/", "http://") // FIX BAD URL FROM BACKEND
                }
                image.isNotEmpty() -> {
                    BASE_IMAGE_URL + image
                }
                else -> ""
            }

            if (finalImageUrl.isNotEmpty()) {
                detailFoodImage.load(finalImageUrl) {
                    placeholder(R.drawable.placeholder)
                    error(R.drawable.placeholder)
                }
            } else {
                detailFoodImage.setImageResource(R.drawable.placeholder)
            }
        }

        // ================= BACK BUTTON =================
        binding.imageButton.setOnClickListener {
            finish()
        }

        // ================= ADD TO CART =================
        binding.addItemButton.setOnClickListener {
            addItemToCartApi()
        }
    }

    // ================= ADD TO CART API =================
    private fun addItemToCartApi() {

        val user = UserSession.user
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val cartItem = CartItems(
            userUid = user.id.toString(),
            foodName = foodName ?: "",
            foodPrice = foodPrice ?: "0.0",
            foodDescription = foodDescriptions ?: "",
            foodImage = foodImage ?: "",
            foodQuantity = 1,
            foodIngredients = foodIngredients ?: ""
        )

        RetrofitInstance.cartApi.addToCart(cartItem)
            .enqueue(object : Callback<CartItems> {

                override fun onResponse(
                    call: Call<CartItems>,
                    response: Response<CartItems>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@DetailsActivity,
                            "Item added to cart 🛒",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@DetailsActivity,
                            "Failed: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<CartItems>, t: Throwable) {
                    Toast.makeText(
                        this@DetailsActivity,
                        "Error: ${t.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}