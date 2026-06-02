package com.example.admin_gosti.model

data class OrderRequest(
    val userUid: String,
    val userName: String,
    val address: String,
    val phoneNumber: String,
    val totalPrice: Int,
    val itemPushKey: String?,
    val orderTime: Long,
    val foodNames: List<String>,
    val foodImages: List<String>,
    val foodPrices: List<Int>,
    val foodQuantities: List<Int>
)