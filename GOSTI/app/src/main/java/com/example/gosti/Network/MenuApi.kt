package com.example.gosti.Network

import com.example.gosti.Model.MenuItem
import retrofit2.Call
import retrofit2.http.*

interface MenuApi {


    @GET("api/menu")
    fun getAllMenuItems(): Call<List<MenuItem>>

    @GET("api/menu/{id}")
    fun getMenuById(@Path("id") id: Long): Call<MenuItem>

    @GET("api/menu/category/{category}")
    fun getMenuByCategory(@Path("category") category: String): Call<List<MenuItem>>


    // QR will use this
    @GET("api/menu/restaurant/{restaurantId}")
    fun getMenuByRestaurant(
        @Path("restaurantId") restaurantId: Long
    ): Call<List<MenuItem>>
}