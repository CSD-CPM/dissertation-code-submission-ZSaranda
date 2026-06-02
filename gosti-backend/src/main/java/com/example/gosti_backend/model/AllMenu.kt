package com.example.gosti_backend.model

import jakarta.persistence.*

@Entity
@Table(name = "all_menu")
class AllMenu(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false)
        var foodName: String = "",

        @Column(nullable = false)
        var foodPrice: String = "",

        @Column(columnDefinition = "TEXT")
        var foodDescription: String? = null,

        @Column(columnDefinition = "TEXT")
        var foodImage: String? = null,

        var foodIngredient: String? = null,

        var category: String? = null
)