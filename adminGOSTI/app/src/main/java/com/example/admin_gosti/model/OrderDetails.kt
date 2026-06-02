package com.example.admin_gosti.model

import java.io.Serializable

enum class OrderStatus {
    PENDING,
    PENDING_ACCEPTED,
    ACCEPTED,
    DISPATCHED,
    COMPLETED,
}


data class OrderDetails(
    var id: Long? = null,
    var userUid: String? = null,
    var userName: String? = null,
    var address: String? = null,
    var totalPrice: Int = 0,
    var phoneNumber: String? = null,
    var orderAccepted: Boolean = false,
    var paymentReceived: Boolean = false,
    var itemPushKey: String? = null,
    var orderTime: Long = 0,
    var items: MutableList<OrderItem> = mutableListOf(),
    var orderStatus: OrderStatus = OrderStatus.PENDING
) : Serializable
