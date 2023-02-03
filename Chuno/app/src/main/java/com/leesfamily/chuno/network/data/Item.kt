package com.leesfamily.chuno.network.data

data class Item(
    val id: Long,
    val name: String,
    val price: Int,
    val description: String,
    val imgPath: String,
    val forRunner: String
)