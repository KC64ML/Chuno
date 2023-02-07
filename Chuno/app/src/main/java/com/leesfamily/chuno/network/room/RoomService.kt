package com.leesfamily.chuno.network.room

import com.leesfamily.chuno.network.data.RoomForm
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RoomService {

    @GET("room")
    fun getRoomList(@Query("lat") lat: Double, @Query("lng") lng: Double): Call<RoomForm>
}