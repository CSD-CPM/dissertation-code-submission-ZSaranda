package com.example.admin_gosti.model

data class UserModel(
    val id: Long = 0,
    val name: String? = null,
    val nameOfRestaurant: String? = null,
    val email: String? = null,
    val password: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val role: String? = "ADMIN"
)
