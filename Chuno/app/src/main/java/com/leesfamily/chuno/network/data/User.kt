package com.leesfamily.chuno.network.data

data class User(

    val id: Long,

    var email: String,

    val nickname: String?,

    val level: Int,

    val paperCount: Int,

    val runnerPlayCount: Int,

    val runnerWinCount: Int,

    val chaserPlayCount: Int,

    val chaserWinCount: Int,

    val exp: Int,

    val isManager: Boolean,

    val money: Int,

    val profile: UserProfile,

    val inventory: Inventory
)
