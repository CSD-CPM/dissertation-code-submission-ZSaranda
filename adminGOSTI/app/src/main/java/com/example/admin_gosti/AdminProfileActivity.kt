package com.example.admin_gosti

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.admin_gosti.databinding.ActivityAdminProfileBinding
import com.example.admin_gosti.model.UserModel
import com.example.admin_gosti.network.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Response

class AdminProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminProfileBinding
    private var userId: Long = 0   // logged-in admin ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get logged-in admin ID from SharedPreferences
        userId = getSharedPreferences("auth", MODE_PRIVATE)
            .getLong("userId", 0)

        binding.backButton.setOnClickListener { finish() }
        binding.saveInfoButton.setOnClickListener { updateUserData() }

        enableEditing(false)

        binding.editButton.setOnClickListener { toggleEdit() }

        loadUserData()
    }

    private fun enableEditing(enable: Boolean) {
        binding.name.isEnabled = enable
        binding.address.isEnabled = enable
        binding.email.isEnabled = enable
        binding.phone.isEnabled = enable
        binding.password.isEnabled = enable
        binding.saveInfoButton.isEnabled = enable
    }

    private fun toggleEdit() {
        val enable = !binding.saveInfoButton.isEnabled
        enableEditing(enable)
        if (enable) binding.name.requestFocus()
    }

    // ================= LOAD USER =================
    private fun loadUserData() {
        lifecycleScope.launch {
            try {
                val response: Response<UserModel> =
                    ApiClient.userApi.getUser(userId)

                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        fillData(user)
                    } else {
                        showError("User data is empty")
                    }
                } else {
                    showError("Failed: ${response.code()}")
                }

            } catch (e: Exception) {
                showError("Failed to load profile 😒")
            }
        }
    }

    private fun fillData(user: UserModel) {
        binding.name.setText(user.name)
        binding.email.setText(user.email)
        binding.password.setText(user.password)
        binding.phone.setText(user.phone)
        binding.address.setText(user.address)
    }

    // ================= UPDATE USER =================
    private fun updateUserData() {

        val updatedUser = UserModel(
            id = userId,
            name = binding.name.text.toString(),
            email = binding.email.text.toString(),
            password = binding.password.text.toString(),
            phone = binding.phone.text.toString(),
            address = binding.address.text.toString(),
            role = "ADMIN"
        )

        lifecycleScope.launch {
            try {
                val response =
                    ApiClient.userApi.updateUser(userId, updatedUser)

                if (response.isSuccessful) {
                    Toast.makeText(
                        this@AdminProfileActivity,
                        "Profile updated successfully 😊",
                        Toast.LENGTH_SHORT
                    ).show()

                    enableEditing(false)
                } else {
                    showError("Update failed: ${response.code()}")
                }

            } catch (e: Exception) {
                showError("Failed to update profile 😒")
            }
        }
    }

    private fun showError(msg: String) {
        Toast.makeText(this@AdminProfileActivity, msg, Toast.LENGTH_SHORT).show()
    }
}