package com.example.gosti_backend.model


import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0, // Primary key

        var name: String = "",                 // User's name
        var nameOfRestaurant: String? = null,  // Only for admin users (nullable for clients)
        var email: String = "",                // Email
        var password: String = "",             // Password
        var address: String = "",              // Address
        var phone: String = "",                // Phone
        var role: String = "CLIENT"            // "ADMIN" or "CLIENT"
)
