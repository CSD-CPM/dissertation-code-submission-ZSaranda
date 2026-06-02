package com.example.admin_gosti.model

data class AllMenu(
    val id: Long? = null,
    val foodName: String,
    val foodPrice: String,
    val foodDescription: String?,
    val foodImage: String?,
    val foodIngredient: String?,
    val category: String? = null,

)
