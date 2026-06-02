package com.example.gosti.Network

import com.example.gosti.Model.OrderDetails
import com.example.gosti.Model.OrderRequestDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrderApi {

    @POST("/api/orders/place")
    fun placeOrder(
        @Body order: OrderRequestDTO): Call<Void>


    @GET("api/orders/user/{userId}")
    fun getUserOrders(@Path("userId") userId: String): Call<List<OrderDetails>>

    @PUT("api/orders/{id}/payment")
    fun markPaymentReceived(@Path("id") orderId: Long): Call<OrderDetails>

    @DELETE("api/orders/{id}")
    fun deleteOrder(@Path("id") orderId: Long): Call<Void>
}
