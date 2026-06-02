package com.example.admin_gosti.network

import com.example.admin_gosti.model.LoginRequest
import com.example.admin_gosti.model.UserModel
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @GET("api/users/{id}")
    suspend fun getUser(
        @Path("id") id: Long
    ): Response<UserModel>

    @PUT("api/users/{id}")
    suspend fun updateUser(
        @Path("id") id: Long,
        @Body user: UserModel
    ): Response<UserModel>

    @POST("api/users/add")
    suspend fun createUser(
        @Body user: UserModel
    ): Response<UserModel>

    // ✅ FIX ADD THIS
    @POST("api/users/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<UserModel>
}