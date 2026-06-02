package com.example.gosti_backend.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class OrderRequestDTO(

        @field:NotBlank(message = "User UID cannot be blank")
        val userUid: String,

        val userName: String = "",

        @field:NotEmpty(message = "Food names list cannot be empty")
        val foodNames: List<String>,

        @field:NotEmpty(message = "Food images list cannot be empty")
        val foodImages: List<String>,

        @field:NotEmpty(message = "Food prices list cannot be empty")
        val foodPrices: List<Int>,

        @field:NotEmpty(message = "Food quantities list cannot be empty")
        val foodQuantities: List<Int>,

        @field:NotBlank(message = "Address cannot be blank")
        val address: String,

        @field:NotNull(message = "Total price cannot be null")
        val totalPrice: Int,

        @field:NotBlank(message = "Phone number cannot be blank")
        val phoneNumber: String,

        val itemPushKey: String? = null,

        val orderTime: Long? = null,

        val restaurantId: Long?,
        val tableNumber: Int?
)
