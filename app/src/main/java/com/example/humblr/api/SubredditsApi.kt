package com.example.humblr.api

import com.example.humblr.auth.LocalStorage
import com.example.humblr.data.models.Subreddit
import com.example.humblr.data.models.SubredditItems
import retrofit2.Response
import retrofit2.http.*

interface SubredditsApi {

    @GET("/{source}/about")
    suspend fun getSubredditInfo(
        @Header("Authorization") authHeader: String = LocalStorage.getApiKey(),
        @Path("source", encoded=true) source: String?
    ): Response<Subreddit>

    @GET("/subreddits/popular")
    suspend fun getAllPopularSubreddits(
        @Header("Authorization") authHeader: String = LocalStorage.getApiKey(),
        @Query("after") page: String
    ): Response<SubredditItems>

    @GET("/subreddits/mine/subscriber")
    suspend fun getSubscribedSubreddits(
        @Header("Authorization") authHeader: String = LocalStorage.getApiKey(),
        @Query("after") page: String
    ): Response<SubredditItems>

    @POST("/api/subscribe")
    suspend fun subscribeToSubreddit(
        @Header("Authorization") authHeader: String = LocalStorage.getApiKey(),
        @Query("action") action: String,
        @Query("sr") name: String
    )

}