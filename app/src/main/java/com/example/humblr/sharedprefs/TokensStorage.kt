package com.example.humblr.sharedprefs

import android.content.Context
import android.content.Context.MODE_PRIVATE


class TokensStorage (context: Context) {
    private val prefs = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
    private val editor = prefs.edit()

    fun putAccessToken(accessToken: String?) {
        editor.putString(ACCESS_TOKEN, accessToken)
        editor.apply()
    }

    fun putRefreshToken(refreshToken: String?) {
        editor.putString(REFRESH_TOKEN, refreshToken)
        editor.apply()
    }

    fun getRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN, null)
    }

    fun getAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }

    companion object {
        private const val ACCESS_TOKEN = "access"
        private const val REFRESH_TOKEN = "refresh"
        private const val PREFERENCE_NAME = "pref_name"
    }
}