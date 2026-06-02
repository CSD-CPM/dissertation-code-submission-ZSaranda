package com.example.gosti_backend.controller

import com.example.gosti_backend.model.CartItem
import com.example.gosti_backend.repository.CartRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cart")
class CartController(private val cartRepository: CartRepository) {

    @GetMapping("/{userUid}")
    fun getUserCart(@PathVariable userUid: String): List<CartItem> =
            cartRepository.findByUserUid(userUid)

    @PostMapping("/add")
    fun addToCart(@RequestBody cartItem: CartItem): ResponseEntity<CartItem> {
        val savedItem = cartRepository.save(cartItem)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem)
    }

    @PutMapping("/{id}/update")
    fun updateCartItem(@PathVariable id: Long, @RequestBody updatedItem: CartItem): ResponseEntity<CartItem> {
        val existing = cartRepository.findById(id)
        return if (existing.isPresent) {
            val item = existing.get().copy(
                    foodQuantity = updatedItem.foodQuantity,
                    foodPrice = updatedItem.foodPrice
            )
            cartRepository.save(item)
            ResponseEntity.ok(item)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteCartItem(@PathVariable id: Long): ResponseEntity<Void> {
        return if (cartRepository.existsById(id)) {
            cartRepository.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/clear/{userUid}")
    fun clearCart(@PathVariable userUid: String): ResponseEntity<Void> {
        cartRepository.deleteByUserUid(userUid)
        return ResponseEntity.noContent().build()
    }
}
