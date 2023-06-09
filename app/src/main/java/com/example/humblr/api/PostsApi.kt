package com.example.humblr.api

import com.example.humblr.auth.LocalStorage
import com.example.humblr.data.models.RedditItems
import retrofit2.Response
import retrofit2.http.*

interface PostsApi {

    @GET("/r/popular/new")
    suspend fun getPostListingNew(
        @Header("Authorization") authHeader: String = LocalStorage.getApiKey(),
        @Query("after") page: String
    ): Response<RedditItems>

    @GET("/r/popular/best")
    suspend fun getPostListingBest(
        @Header("Authorization") authHeader: String = LocalStorage.getApiKey(),
        @Query("after") page: String
    ): Response<RedditItems>

    @GET("/r/all/search")
    suspend fun getPostListingSearch(
        @Header("Authorization") authHeader: String = LocalStorage.getApiKey(),
        @Query("after") page: String,
        @Query("q") query: String
    ): Response<RedditItems>

    @GET("{url}")
    suspend fun getSinglePostAndComments(
        @Header("Authorization") authHeader: String = LocalStorage.getApiKey(),
        @Path("url", encoded=true) url: String
    ): Response<List<RedditItems>>

    @GET("/new")
    suspend fun getPostsFromMySubreddits(
        @Header("Authorization") authHeader: String = LocalStorage.getApiKey(),
        @Query("after") page: String
    ): Response<RedditItems>

    @GET("user/{username}/saved?type=links")
    suspend fun getMySavedPosts(
        @Header("Authorization") authHeader: String = LocalStorage.getApiKey(),
        @Path("username", encoded=true) username: String?,
        @Query("after") page: String

    ): Response<RedditItems>

    @POST("/api/save")
    suspend fun savePost(
        @Header("Authorization") authHeader: String = LocalStorage.getApiKey(),
        @Query("id") fullName: String
    )

    @POST("/api/unsave")
    suspend fun unsavePost(
        @Header("Authorization") authHeader: String = LocalStorage.getApiKey(),
        @Query("id") fullName: String
    )

    @POST("/api/vote")
    suspend fun vote(
        @Header("Authorization") authHeader: String = LocalStorage.getApiKey(),
        @Query("dir") dir: Int,
        @Query("id") fullName: String
    )

}