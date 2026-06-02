package com.example.gosti

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gosti.Model.LoginRequest
import com.example.gosti.Model.UserModel
import com.example.gosti.Network.RetrofitInstance
import com.example.gosti.databinding.ActivityLoginBinding
import com.example.gosti.session.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load user from SharedPreferences if exists
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
            goToMain()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.emailAddress.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password 😒", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        binding.donthavebutton.setOnClickListener {
            startActivity(Intent(this, SignActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        val request = LoginRequest(email, password)

        RetrofitInstance.userApi.login(request).enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        Toast.makeText(this@LoginActivity, "Login successful 😁", Toast.LENGTH_SHORT).show()
                        saveUserLocally(user)
                        goToMain()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed 😒", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Login failed 😒"
                    Toast.makeText(this@LoginActivity, errorMsg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Network error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserLocally(user: UserModel) {
        // Save to memory
        UserSession.user = user

        // Save to SharedPreferences
        val sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putLong("user_id", user.id ?: 0L)
            putString("user_name", user.name)
            putString("user_email", user.email)
            putString("user_address", user.address)
            putString("user_phone", user.phone)
            apply()
        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
