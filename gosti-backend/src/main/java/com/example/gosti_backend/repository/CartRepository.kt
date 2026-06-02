package com.example.gosti_backend.repository


import com.example.gosti_backend.model.CartItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CartRepository : JpaRepository<CartItem, Long> {
    fun findByUserUid(userUid: String): List<CartItem>
    fun deleteByUserUid(userUid: String)
}
