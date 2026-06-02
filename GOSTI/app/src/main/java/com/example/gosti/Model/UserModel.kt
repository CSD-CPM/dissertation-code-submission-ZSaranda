package com.example.gosti.Model

data class UserModel(
    val id: Long?,
    val name: String,
    val nameOfRestaurant: String?,
    val email: String,
    val password: String,
    val address: String,
    val phone: String,
    val role: String
)
