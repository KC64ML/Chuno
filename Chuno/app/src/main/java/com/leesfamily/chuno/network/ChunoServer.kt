package com.leesfamily.chuno.network

import com.google.gson.GsonBuilder
import com.leesfamily.chuno.BuildConfig
import com.leesfamily.chuno.network.item.ItemService
import com.leesfamily.chuno.network.login.LoginService
import com.leesfamily.chuno.network.room.RoomService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ChunoServer {
    private const val apiBaseUrl = BuildConfig.SERVER_URL

    var gson = GsonBuilder().setLenient().create()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson)) // gson converter 생성(gson은 JSON을 자바클래스로 변경)
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