package com.leesfamily.chuno.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LoginServer {
    private const val apiBaseUrl = "http://jsonplaceholder.typicode.com/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create()) // gson converter 생성(gson은 JSON을 자바클래스로 변경)
            .build()
    }

    val loginServer: LoginService by lazy {
        retrofit.create(LoginService::class.java)
    }

}