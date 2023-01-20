package com.leesfamily.chuno.network

import com.leesfamily.chuno.network.data.User
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {
    @FormUrlEncoded
    @GET("/posts")
    fun getData(@Query("userId") id: String): Call<List<User>>

    @FormUrlEncoded
    @POST("/posts")
    fun postData(@FieldMap param: HashMap<String, Object>): Call<User>
}

