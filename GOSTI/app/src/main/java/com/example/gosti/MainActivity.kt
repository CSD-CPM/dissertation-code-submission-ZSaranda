package com.example.gosti

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gosti.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gosti.Fragment.NotificationBottomFragment
import com.example.gosti.Model.UserModel
import com.example.gosti.session.UserSession

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ================= Initialize UserSession =================
        initializeUserSession()

        // ================= Navigation =================
        val navController = findNavController(R.id.fragmentContainerView)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setupWithNavController(navController)

        // ================= Notifications =================
        binding.notificationButton.setOnClickListener {
            val bottomSheetDialog = NotificationBottomFragment()
            bottomSheetDialog.show(supportFragmentManager, "Notification")
        }

        // ================= HANDLE QR / DEEP LINK =================
        handleDeepLink(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent) {

        val data: Uri? = intent.data

        if (data != null) {

            val restaurantId = data.getQueryParameter("restaurantId")
            val tableNumber = data.getQueryParameter("tableNumber")

            if (!restaurantId.isNullOrEmpty() && !tableNumber.isNullOrEmpty()) {

                openMenu(restaurantId, tableNumber)
            }
        }
    }

    private fun openMenu(restaurantId: String, tableNumber: String) {

        // Save table context
        val sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        sharedPref.edit()
            .putString("restaurant_id", restaurantId)
            .putString("table_number", tableNumber)
            .apply()

        // Navigate to HOME (your menu/home fragment)
        val navController = findNavController(R.id.fragmentContainerView)

        val bundle = Bundle().apply {
            putString("restaurantId", restaurantId)
            putString("tableNumber", tableNumber)
        }

        
        navController.navigate(R.id.homeFragment, bundle)
    }

    // ================= USER SESSION =================
    private fun initializeUserSession() {
        val sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getLong("user_id", 0L)

        if (userId != 0L) {
            UserSession.user = UserModel(
                id = userId,
                name = sharedPref.getString("user_name", "") ?: "",
                nameOfRestaurant = null,
                email = sharedPref.getString("user_email", "") ?: "",
                password = "",
                address = sharedPref.getString("user_address", "") ?: "",
                phone = sharedPref.getString("user_phone", "") ?: "",
                role = ""
            )
        }
    }
}