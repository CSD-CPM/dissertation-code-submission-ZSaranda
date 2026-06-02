package com.example.gosti.Network

import com.example.gosti.Model.LoginRequest
import com.example.gosti.Model.SignUpRequest
import com.example.gosti.Model.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {


        @POST("api/users/register")  // matches backend
        fun signUp(@Body signUpRequest: SignUpRequest): Call<UserModel>

        @POST("api/users/login")  // matches backend
        fun login(@Body loginRequest: LoginRequest): Call<UserModel>

        @GET("api/users/{id}")
        fun getUser(@Path("id") id: Long): Call<UserModel>


}
