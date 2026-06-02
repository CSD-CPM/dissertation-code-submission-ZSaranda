package com.example.gosti_backend.model

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
@Table(name = "order_items")
data class OrderItem(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column(name = "food_name")
        var name: String = "",

        @Column(name = "food_image")
        var image: String = "",

        @Column(name = "food_price")
        var price: Int = 0,

        @Column(name = "food_quantity")
        var quantity: Int = 0,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "order_id")
        @JsonBackReference
        var order: OrderDetails? = null
)
