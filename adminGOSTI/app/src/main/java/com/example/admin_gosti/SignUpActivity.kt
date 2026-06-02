package com.example.admin_gosti

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admin_gosti.databinding.ActivitySignUpBinding
import com.example.admin_gosti.model.UserModel
import com.example.admin_gosti.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    // ✅ FIXED: use userApi (NOT api)
    private val userApi by lazy { ApiClient.userApi }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.createUserButton.setOnClickListener {
            val userName = binding.name.text.toString().trim()
            val restaurantName = binding.restaurantName.text.toString().trim()
            val email = binding.emailOrPhone.text.toString().trim()
            val password = binding.passsword.text.toString().trim()

            if (userName.isBlank() || restaurantName.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(userName, restaurantName, email, password)
            }
        }

        binding.alreadyHaveAccountButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun createAccount(
        userName: String,
        restaurantName: String,
        email: String,
        password: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val newUser = UserModel(
                    id = 0L,
                    name = userName,
                    nameOfRestaurant = restaurantName,
                    email = email,
                    password = password,
                    role = "ADMIN"
                )


                val response = userApi.createUser(newUser)

                withContext(Dispatchers.Main) {

                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Account created successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                        finish()

                    } else {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Failed: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Account creation failed: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()

                    Log.e("SignUp", "createAccount error", e)
                }
            }
        }
    }
}