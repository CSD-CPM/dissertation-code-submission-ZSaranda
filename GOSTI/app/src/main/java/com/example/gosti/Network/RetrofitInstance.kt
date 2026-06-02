package com.example.gosti.Network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://telegrammatic-unawarely-clementina.ngrok-free.dev/"

    private val retrofit by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    val cartApi: CartApi by lazy {
        retrofit.create(CartApi::class.java)
    }

    val menuApi: MenuApi by lazy {
        retrofit.create(MenuApi::class.java)
    }

    val orderApi: OrderApi by lazy {
        retrofit.create(OrderApi::class.java)
    }
}
