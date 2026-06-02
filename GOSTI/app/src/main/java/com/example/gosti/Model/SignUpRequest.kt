package com.example.gosti.Model

data class SignUpRequest(
    val name: String,
    val nameOfRestaurant: String? = null,
    val email: String,
    val password: String,
    val address: String,
    val phone: String,
    val role: String = "CLIENT"
)
