package com.leesfamily.chuno.network.data

import android.graphics.Point

data class Room(
     var id: Long,

     var title: String,

     var password: String?,

     var isPublic: String,

     var lat: Double,

     var lng: Double,

     var currentPlayers: Int = 1,

     var maxPlayers: Int = 10,

     var location: Point,

     var radius: Int,

     var dateTime: DateTime,

     var host: User,

     var distance: Double,

     var isPushed: Boolean
)
