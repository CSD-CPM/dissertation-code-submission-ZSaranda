package com.example.admin_gosti

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.admin_gosti.databinding.ActivityLoginBinding
import com.example.admin_gosti.model.LoginRequest
import com.example.admin_gosti.model.UserModel
import com.example.admin_gosti.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    // ✅ FIX: use userApi (NOT api)
    private val userApi by lazy { ApiClient.userApi }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (isUserLoggedIn()) {
            goToMainActivity()
            return
        }

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

        binding.dontHaveAccountButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {

        val loginRequest = LoginRequest(email, password)

        lifecycleScope.launch(Dispatchers.IO) {
            try {


                val response = userApi.login(loginRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        val user = response.body()

                        if (user != null && user.id != 0L) {
                            saveUser(user)
                            Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                            goToMainActivity()
                        } else {
                            Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Server error: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, e.message ?: "Login error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val prefs = getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
        return prefs.getLong("USER_ID", 0L) != 0L
    }

    private fun saveUser(user: UserModel) {
        val prefs = getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
        prefs.edit()
            .putLong("USER_ID", user.id)
            .putString("USER_NAME", user.name)
            .apply()
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
