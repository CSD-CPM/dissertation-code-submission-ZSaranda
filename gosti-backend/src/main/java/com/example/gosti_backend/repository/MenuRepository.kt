package com.example.gosti_backend.repository


import com.example.gosti_backend.model.AllMenu
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MenuRepository : JpaRepository<AllMenu, Long> {
    fun findByCategory(category: String): List<AllMenu>
}
