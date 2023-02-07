package com.leesfamily.chuno.network.login

import com.leesfamily.chuno.network.data.DataForm
import com.leesfamily.chuno.network.data.LoginForm
import com.leesfamily.chuno.network.data.NickForm
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface LoginService {

    @POST("kakao/mobile/login")
    fun login(@Body token: String): Call<LoginForm>

    @Headers("Content-Type:text/plain")
    @POST("kakao/tokenConfirm")
    fun tokenConfirm(@Body token: RequestBody): Call<Int>

    @GET("user")
    fun getUserData(@Header("Authorization") auth: String): Call<DataForm>

    @GET("user/nickname/{nickname}")
    fun getNickDuplic(@Path("nickname") nickname: String): Call<NickForm>
//
//    @FormUrlEncoded
//    @GET("/posts")
//    fun getData(@Query("userId") id: String): Call<List<User>>
//
//    @FormUrlEncoded
//    @POST("/posts")
//    fun postData(@FieldMap param: HashMap<String, Object>): Call<User>
}