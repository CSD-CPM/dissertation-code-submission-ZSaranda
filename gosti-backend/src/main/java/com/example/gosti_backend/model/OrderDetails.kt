package com.example.gosti_backend.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "customer_orders")
data class OrderDetails(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column(nullable = false)
        var userUid: String = "",

        var userName: String = "",

        @Column(nullable = false)
        var address: String = "",

        @Column(nullable = false)
        var totalPrice: Int = 0,

        @Column(nullable = false)
        var phoneNumber: String = "",

        // 🔥 QR SUPPORT ADDED
        @Column(nullable = true)
        var restaurantId: Long? = null,

        @Column(nullable = true)
        var tableNumber: Int? = null,


        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        var orderStatus: OrderStatus = OrderStatus.PENDING,

        @Column(nullable = false)
        var paymentReceived: Boolean = false,

        var itemPushKey: String? = null,

        @Column(nullable = false)
        var orderTime: Long = Instant.now().toEpochMilli(),

        @OneToMany(
                mappedBy = "order",
                cascade = [CascadeType.ALL],
                orphanRemoval = true
        )
        @JsonManagedReference
        var items: MutableList<OrderItem> = mutableListOf()
) {

        fun addItem(item: OrderItem) {
                items.add(item)
                item.order = this
        }
}