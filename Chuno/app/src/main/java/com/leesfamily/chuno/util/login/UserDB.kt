package com.leesfamily.chuno.util.login

import com.leesfamily.chuno.network.data.User


object UserDB {

    private var isConfirmToken: Boolean = false
    private var email: String? = null

    fun setEmail(token: String) {
        this.email = token
    }

    fun getEmail() = email


    fun setIsConfirmToken(isConfirmToken: Boolean) {
        this.isConfirmToken = isConfirmToken
    }

    fun getIsConfirmToken() = isConfirmToken

}