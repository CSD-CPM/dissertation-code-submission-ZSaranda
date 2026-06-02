package com.example.gosti.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.gosti.LoginActivity
import com.example.gosti.Model.UserModel
import com.example.gosti.Network.RetrofitInstance
import com.example.gosti.databinding.FragmentProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private var userId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Get logged in user ID from SharedPreferences
        val sharedPref = requireContext().getSharedPreferences("MyAppPrefs", 0)
        userId = sharedPref.getLong("user_id", 0)

        if (userId == 0L) {
            // No user logged in, go to login
            goToLogin()
        } else {
            loadUserData()
        }

        binding.logoutbutton.setOnClickListener {
            logout()
        }

        return binding.root
    }

    private fun loadUserData() {
        RetrofitInstance.userApi.getUser(userId).enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        binding.name.text = it.name
                        binding.email.text = it.email
                        binding.address.text = it.address
                        binding.phone.text = it.phone
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun logout() {
        // Clear SharedPreferences
        val sharedPref = requireContext().getSharedPreferences("MyAppPrefs", 0)
        with(sharedPref.edit()) {
            clear()
            apply()
        }

        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
        goToLogin()
    }

    private fun goToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
