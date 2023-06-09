package com.example.humblr.data

import com.example.humblr.sharedprefs.TokensStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokensRepository @Inject constructor(
    private val tokensStorage: TokensStorage
) {
    fun addAccessTokenToLocalStorage(token: String?) {
        tokensStorage.putAccessToken(token)
    }

    fun addRefreshTokenToLocalStorage(token: String?) {
        tokensStorage.putRefreshToken(token)
    }

    fun getAccessTokenFromLocalStorage(): String? {
        return tokensStorage.getAccessToken()
    }

    fun getRefreshTokenFromLocalStorage(): String? {
        return tokensStorage.getRefreshToken()
    }
}