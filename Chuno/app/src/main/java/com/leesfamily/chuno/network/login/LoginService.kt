package com.leesfamily.chuno.network.login

import com.leesfamily.chuno.network.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

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

    @Multipart
    @POST("kakao/register")
    fun registerUser(
        @Part file: MultipartBody.Part,
        @Part nickname: MultipartBody.Part,
        @Part email: MultipartBody.Part?
    ): Call<String>

    @Multipart
    @PUT("user/profile")
    fun profileImage(
        @Header("Authorization") auth: String,
        @Part nickname: MultipartBody.Part,
        @Part file: MultipartBody.Part
    ): Call<DataForm>

    @DELETE("user")
    fun deleteUser(@Header("Authorization") auth: String): Call<DataForm>


//
//    @FormUrlEncoded
//    @GET("/posts")
//    fun getData(@Query("userId") id: String): Call<List<User>>
//
//    @FormUrlEncoded
//    @POST("/posts")
//    fun postData(@FieldMap param: HashMap<String, Object>): Call<User>
}