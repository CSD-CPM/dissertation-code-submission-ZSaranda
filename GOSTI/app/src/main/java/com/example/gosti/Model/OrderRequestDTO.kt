package com.example.gosti.Model


data class OrderRequestDTO(
    val userUid: String,
    val userName: String,
    val foodNames: List<String>,
    val foodImages: List<String>,
    val foodPrices: List<Int>,
    val foodQuantities: List<Int>,
    val address: String,
    val totalPrice: Int,
    val phoneNumber: String,
    val itemPushKey: String?,
    val orderTime: Long?,
    val restaurantId: String?,
    val tableNumber: String?
)
