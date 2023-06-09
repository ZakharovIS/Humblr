package com.example.humblr.api

import com.example.humblr.auth.LocalStorage
import com.example.humblr.data.models.*
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.http.*

interface UsersApi {

    @GET("user/{source}/about")
    suspend fun getUserInfo(
        @Header("Authorization") authHeader: String = LocalStorage.getApiKey(),
        @Path("source", encoded=true) source: String?
    ): Response<User>

    @GET("/api/v1/me")
    suspend fun getMyProfile(
        @Header("Authorization") authHeader: String = LocalStorage.getApiKey()
    ): Response<MyData>

    @GET("/api/v1/me/friends")
    suspend fun getMyFriends(
        @Header("Authorization") authHeader: String = LocalStorage.getApiKey()
    ): Response<Friends>

    @PUT("/api/v1/me/friends/{username}")
    suspend fun addToFriends(
        @Header("Authorization") authHeader: String = LocalStorage.getApiKey(),
        @Path("username") username: String,
        @Body requestBody: RequestBody = "{\"name\": \"${username}\"}".toRequestBody()
    ): Response<FriendData>

    @DELETE("/api/v1/me/friends/{username}")
    suspend fun deleteFromFriends(
        @Header("Authorization") authHeader: String = LocalStorage.getApiKey(),
        @Path("username", encoded=true) username: String
    ): Response<FriendData>
}