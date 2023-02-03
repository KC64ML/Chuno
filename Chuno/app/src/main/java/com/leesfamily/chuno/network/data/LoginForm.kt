package com.leesfamily.chuno.network.data

data class LoginForm(
    val result: String,
    val code: String
)
data class DataForm(
    val result: User,
    val code: Int
)