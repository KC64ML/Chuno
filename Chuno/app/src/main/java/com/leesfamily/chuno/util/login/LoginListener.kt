package com.leesfamily.chuno.util.login

interface LoginListener {
    fun loginSuccess(token:String)
    fun loginFailed()
}