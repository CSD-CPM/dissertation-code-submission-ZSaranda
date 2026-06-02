package com.example.gosti

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gosti.Model.LoginRequest
import com.example.gosti.Model.SignUpRequest
import com.example.gosti.Model.UserModel
import com.example.gosti.Network.RetrofitInstance
import com.example.gosti.databinding.ActivitySignBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createAccountButton.setOnClickListener {
            val name = binding.userName.text.toString().trim()
            val email = binding.emailAddress.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val address = binding.address.text.toString().trim()
            val phone = binding.phone.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all fields 😒", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signUpUser(name, email, password, address, phone)
        }

        binding.alreadyhavebutton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun signUpUser(
        name: String,
        email: String,
        password: String,
        address: String,
        phone: String
    ) {
        val request = SignUpRequest(name, email = email, password = password, address = address, phone = phone)

        RetrofitInstance.userApi.signUp(request).enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        // Save user locally to stay logged in
                        saveUserLocally(user)

                        Toast.makeText(this@SignActivity, "Account created successfully 😁", Toast.LENGTH_SHORT).show()
                        goToMain()
                    } else {
                        Toast.makeText(this@SignActivity, "Sign up failed 😒", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Sign up failed 😒"
                    Toast.makeText(this@SignActivity, errorMsg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Toast.makeText(this@SignActivity, "Network error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserLocally(user: UserModel) {
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
