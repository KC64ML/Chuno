package com.leesfamily.chuno.util.login

object UserDB {
    private var email: String? = null
    private var token: String? = null

    fun setEmail(email: String) {
        UserDB.email = email
    }

    fun setToken(token: String?) {
        UserDB.token = token
        if (token == null) {

        }
    }

    fun getEmail(){

    }

    fun getToken()= token

    fun isLogin():Boolean{
        return token != null
    }
}