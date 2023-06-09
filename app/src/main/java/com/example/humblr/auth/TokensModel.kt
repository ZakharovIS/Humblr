package com.example.humblr.auth

data class TokensModel(
    val accessToken: String,
    val refreshToken: String,
    val idToken: String
)
