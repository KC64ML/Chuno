package com.leesfamily.chuno.network.data

// Resource<Data<out<T>>
data class LoginForm(
    val result: String,
    val code: String
)

data class DataForm(
    val result: User,
    val code: Int
)

data class ItemForm(
    val result: List<Item>,
    val code: Int
)

data class NickForm(
    val result: Int,
    val code: Int
)

data class RoomForm(
    val result: List<Room>,
    val code: Int
)

sealed class Resource<out T> {
    class Success<out T> : Resource<T>()
    class Failure<out T> : Resource<T>()
}