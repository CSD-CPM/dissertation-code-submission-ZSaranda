package com.example.admin_gosti.network

import com.example.admin_gosti.model.AllMenu
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface MenuApi {


    @POST("api/menu/add")
    suspend fun createMenuItem(
        @Body menuItem: AllMenu
    ): Response<AllMenu>


    @GET("api/menu")
    suspend fun getAllMenuItems(): Response<List<AllMenu>>


    @Multipart
    @POST("api/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): Response<ResponseBody>

    @DELETE("api/menu/{id}")
    suspend fun deleteMenuItem(
        @Path("id") id: Long
    ): Response<Unit>
}