package com.example.humblr.auth

object LocalStorage {
    var accessToken: String? = null
    var refreshToken: String? = null
    var idToken: String? = null
    var logout: Boolean = false
    var myName: String? = null

    fun getApiKey(): String {
        return "Bearer $accessToken"
    }
}


