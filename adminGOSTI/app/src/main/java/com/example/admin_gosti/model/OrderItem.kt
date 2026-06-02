package com.example.admin_gosti.model

import java.io.Serializable

data class OrderItem(
    val name: String? = null,
    val image: String? = null,
    val quantity: Int = 0,
    val price: Double =  0.0
) : Serializable
