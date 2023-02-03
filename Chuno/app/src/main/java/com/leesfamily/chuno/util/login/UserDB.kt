package com.leesfamily.chuno.util.login

import com.leesfamily.chuno.network.data.User


object UserDB {

    private var token: String? = null
    private var user: User? = null

    fun setEmail(email: String) {
        this.user?.email = email
    }

    fun setUser(user: User?) {
        this.user = user
    }

    fun getEmail(): String? {
        return user?.email
    }

    fun setToken(token: String) {
        this.token = token
    }

    fun getToken() = token

    fun isLogin(): Boolean {
        return token != null
    }
}