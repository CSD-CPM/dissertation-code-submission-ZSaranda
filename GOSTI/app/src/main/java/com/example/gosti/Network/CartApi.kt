package com.example.gosti.Network

import com.example.gosti.Model.CartItems
import com.example.gosti.Model.OrderDetails
import retrofit2.Call
import retrofit2.http.*

interface CartApi {

    @GET("api/cart/{userUid}")
    fun getCartItems(
        @Path("userUid") userUid: String
    ): Call<List<CartItems>>

    @POST("api/cart/add")
    fun addToCart(
        @Body cartItem: CartItems
    ): Call<CartItems>

    @PUT("api/cart/{id}/update")
    fun updateCartItem(
        @Path("id") id: Long,
        @Body cartItem: CartItems
    ): Call<CartItems>

    @DELETE("api/cart/{id}")
    fun deleteCartItem(
        @Path("id") id: Long
    ): Call<Void>
}



