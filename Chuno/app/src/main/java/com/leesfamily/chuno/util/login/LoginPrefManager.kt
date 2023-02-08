package com.leesfamily.chuno.util.login

import android.content.Context
import android.util.Log
import com.leesfamily.chuno.App

object LoginPrefManager {

    private const val LOGIN_KEY = "login_access_token"
    private const val IS_CONFIRM_TOKEN = "is_confirm_token"

    fun setLastLoginToken(token: String?) {
        Log.d("추노", "setLastLoginToken: set $token")
        App.context.getSharedPreferences(LOGIN_KEY, Context.MODE_PRIVATE)
            .edit()
            .putString(LOGIN_KEY, token)
            .apply()
    }

    fun removeLastLoginToken() {
        App.context.getSharedPreferences(LOGIN_KEY, Context.MODE_PRIVATE)
            .edit()
            .remove(LOGIN_KEY)
            .apply()
    }

    fun getLastLoginToken(): String? {
        return App.context.getSharedPreferences(LOGIN_KEY, Context.MODE_PRIVATE)
            .getString(LOGIN_KEY, null)
    }
}