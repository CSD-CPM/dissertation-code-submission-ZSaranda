package com.example.gosti_backend.repository

import com.example.gosti_backend.model.OrderDetails
import com.example.gosti_backend.model.OrderStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<OrderDetails, Long> {

    // User order history
    fun findByUserUid(userUid: String): List<OrderDetails>


    fun findByOrderStatus(orderStatus: OrderStatus): List<OrderDetails>


    fun findByOrderStatusIn(statuses: List<OrderStatus>): List<OrderDetails>


    fun findByUserUidAndOrderStatus(
            userUid: String,
            orderStatus: OrderStatus
    ): List<OrderDetails>
}
