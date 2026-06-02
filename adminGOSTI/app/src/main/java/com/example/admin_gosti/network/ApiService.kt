package com.example.admin_gosti.network

import com.example.admin_gosti.model.OrderDetails
import com.example.admin_gosti.model.OrderRequest
import com.example.admin_gosti.model.AllMenu
import com.example.admin_gosti.model.LoginRequest
import com.example.admin_gosti.model.UserModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ----- Menu operations -----
    @POST("/api/menu/add")
    suspend fun createMenuItem(@Body menuItem: AllMenu): Response<AllMenu>

    @GET("/api/menu")
    suspend fun getAllMenuItems(): Response<List<AllMenu>>

    @GET("/api/menu/{id}")
    suspend fun getMenuItemById(@Path("id") id: Long): Response<AllMenu>

    @PUT("/api/menu/{id}")
    suspend fun updateMenuItem(@Path("id") id: Long, @Body menuItem: AllMenu): Response<AllMenu>

    @DELETE("/api/menu/{id}")
    suspend fun deleteMenuItem(@Path("id") id: Long): Response<Unit>

    @GET("/api/menu/category/{category}")
    suspend fun getMenuItemsByCategory(@Path("category") category: String): Response<List<AllMenu>>

    // ----- User operations -----
    @POST("/api/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<UserModel>

    @GET("/api/users/{id}")
    suspend fun getUser(@Path("id") id: Long): Response<UserModel>

    @POST("/api/users/add")
    suspend fun createUser(@Body user: UserModel): Response<UserModel>

    @PUT("/api/users/{id}")
    suspend fun updateUser(@Path("id") id: Long, @Body user: UserModel): Response<UserModel>

    @DELETE("/api/users/{id}")
    suspend fun deleteUser(@Path("id") id: Long): Response<Unit>

    // ----- Orders operations -----
    @GET("/api/orders/pending")
    suspend fun getPendingOrders(): Response<List<OrderDetails>>

    @GET("/api/orders/completed")
    suspend fun getCompletedOrders(): Response<List<OrderDetails>>

    @GET("/api/orders/dispatched")
    suspend fun getDispatchedOrders(): Response<List<OrderDetails>>

    @PUT("/api/orders/{id}/accept")
    suspend fun acceptOrder(@Path("id") orderId: Long): Response<OrderDetails>

    @PUT("/api/orders/{id}/dispatch")
    suspend fun dispatchOrder(@Path("id") orderId: Long): Response<OrderDetails>

    @PUT("/api/orders/{id}/complete")
    suspend fun completeOrder(@Path("id") orderId: Long): Response<OrderDetails>

    @PUT("/api/orders/{id}/payment")
    suspend fun markPaymentReceived(@Path("id") orderId: Long): Response<OrderDetails>

    @POST("/api/orders/place")
    suspend fun placeOrder(@Body orderRequest: OrderRequest): Response<OrderDetails>

    @GET("/api/orders/user/{userUid}")
    suspend fun getUserOrders(@Path("userUid") userUid: String): Response<List<OrderDetails>>
}
