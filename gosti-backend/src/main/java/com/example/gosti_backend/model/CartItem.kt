package com.example.gosti_backend.model



import jakarta.persistence.*

@Entity
@Table(name = "cart_items")
data class CartItem(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        val userUid: String = "",          // Which user owns this cart
        val foodName: String = "",
        val foodPrice: String = "",
        val foodDescription: String = "",
        val foodImage: String = "",
        val foodQuantity: Int = 1,
        val foodIngredients: String? = null
)
