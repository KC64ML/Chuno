package com.leesfamily.chuno.network

import com.leesfamily.chuno.network.item.ItemService
import com.leesfamily.chuno.network.login.LoginService
import com.leesfamily.chuno.network.room.RoomService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ChunoServer {
    private const val apiBaseUrl = "https://i8d208.p.ssafy.io/api/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create()) // gson converter 생성(gson은 JSON을 자바클래스로 변경)
            .build()
    }

    val loginServer: LoginService by lazy {
        retrofit.create(LoginService::class.java)
    }

    val itemServer: ItemService by lazy {
        retrofit.create(ItemService::class.java)
    }

    val roomServer: RoomService by lazy {
        retrofit.create(RoomService::class.java)
    }
}