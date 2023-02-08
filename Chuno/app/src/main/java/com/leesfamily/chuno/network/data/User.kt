package com.leesfamily.chuno.network.data

data class User(

    val id: Long,

    var email: String,

    var phone: String,

    val nickname: String?,

    val level: Int,

    val paperCount: Int,

    val catchCount: Int,

    val runnerPlayCount: Int,

    val runnerWinCount: Int,

    val chaserPlayCount: Int,

    val chaserWinCount: Int,

    val exp: Int,

    val manager: Boolean,

    val money: Int,

    val profile: UserProfile?,

//    val inventory: List<Inventory>,

    var isDeleted: Boolean,

    var items: List<Int>
)

data class RegisterUser(
    var nickname: String,
    var email: String
)
