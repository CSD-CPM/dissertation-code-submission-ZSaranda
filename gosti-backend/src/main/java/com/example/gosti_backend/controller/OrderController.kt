package com.example.gosti_backend.controller

import com.example.gosti_backend.dto.OrderRequestDTO
import com.example.gosti_backend.model.OrderDetails
import com.example.gosti_backend.model.OrderItem
import com.example.gosti_backend.model.OrderStatus
import com.example.gosti_backend.repository.OrderRepository
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(
        private val orderRepository: OrderRepository
) {

    // ================= PLACE ORDER (QR UPDATED) =================
    @PostMapping("/place")
    fun placeOrder(
            @Valid @RequestBody request: OrderRequestDTO
    ): ResponseEntity<OrderDetails> {

        if (
                request.foodNames.size != request.foodImages.size ||
                request.foodNames.size != request.foodPrices.size ||
                request.foodNames.size != request.foodQuantities.size
        ) {
            return ResponseEntity.badRequest().build()
        }

        val order = OrderDetails(
                userUid = request.userUid,
                userName = request.userName,
                address = request.address,
                phoneNumber = request.phoneNumber,
                totalPrice = request.totalPrice,
                itemPushKey = request.itemPushKey,
                orderTime = request.orderTime ?: System.currentTimeMillis(),
                orderStatus = OrderStatus.PENDING,

                // 🔥 QR FIELDS ADDED
                restaurantId = request.restaurantId,
                tableNumber = request.tableNumber
        )

        request.foodNames.indices.forEach { i ->
            order.addItem(
                    OrderItem(
                            name = request.foodNames[i],
                            image = request.foodImages[i],
                            price = request.foodPrices[i],
                            quantity = request.foodQuantities[i]
                    )
            )
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderRepository.save(order))
    }

    // ----- Get orders for a user -----
    @GetMapping("/user/{userUid}")
    fun getUserOrders(@PathVariable userUid: String): ResponseEntity<List<OrderDetails>> {
        return ResponseEntity.ok(orderRepository.findByUserUid(userUid))
    }

    // ----- Get pending orders -----
    @GetMapping("/pending")
    fun getPendingOrders(): ResponseEntity<List<OrderDetails>> {
        val pendingOrders = orderRepository.findByOrderStatusIn(
                listOf(OrderStatus.PENDING, OrderStatus.PENDING_ACCEPTED)
        )
        return ResponseEntity.ok(pendingOrders)
    }

    // ----- Accept order -----
    @PutMapping("/{orderId}/accept")
    fun acceptOrder(@PathVariable orderId: Long): ResponseEntity<OrderDetails> {
        val order = orderRepository.findById(orderId)
                .orElseThrow { RuntimeException("Order not found") }

        order.orderStatus = OrderStatus.PENDING_ACCEPTED
        return ResponseEntity.ok(orderRepository.save(order))
    }

    // ----- Dispatch order -----
    @PutMapping("/{orderId}/dispatch")
    fun dispatchOrder(@PathVariable orderId: Long): ResponseEntity<OrderDetails> {
        val order = orderRepository.findById(orderId)
                .orElseThrow { RuntimeException("Order not found") }

        order.orderStatus = OrderStatus.DISPATCHED
        return ResponseEntity.ok(orderRepository.save(order))
    }

    // ----- Mark payment as received -----
    @PutMapping("/{orderId}/payment")
    fun markPaymentReceived(@PathVariable orderId: Long): ResponseEntity<OrderDetails> {
        val order = orderRepository.findById(orderId)
                .orElseThrow { RuntimeException("Order not found") }

        if (order.orderStatus != OrderStatus.DISPATCHED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(order)
        }

        order.paymentReceived = true
        return ResponseEntity.ok(orderRepository.save(order))
    }

    // ----- Complete order -----
    @PutMapping("/{orderId}/complete")
    fun completeOrder(@PathVariable orderId: Long): ResponseEntity<OrderDetails> {
        val order = orderRepository.findById(orderId)
                .orElseThrow { RuntimeException("Order not found") }

        if (order.orderStatus != OrderStatus.DISPATCHED || !order.paymentReceived) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(order)
        }

        order.orderStatus = OrderStatus.COMPLETED
        return ResponseEntity.ok(orderRepository.save(order))
    }

    // ----- Get dispatched orders -----
    @GetMapping("/dispatched")
    fun getDispatchedOrders(): ResponseEntity<List<OrderDetails>> {
        return ResponseEntity.ok(
                orderRepository.findByOrderStatus(OrderStatus.DISPATCHED)
        )
    }

    // ----- Get completed orders -----
    @GetMapping("/completed")
    fun getCompletedOrders(): ResponseEntity<List<OrderDetails>> {
        return ResponseEntity.ok(
                orderRepository.findByOrderStatus(OrderStatus.COMPLETED)
        )
    }

    // ----- Get specific order -----
    @GetMapping("/{orderId}")
    @Transactional
    fun getOrderDetails(@PathVariable orderId: Long): ResponseEntity<OrderDetails> {
        return ResponseEntity.ok(
                orderRepository.findById(orderId)
                        .orElseThrow { RuntimeException("Order not found") }
        )
    }
}