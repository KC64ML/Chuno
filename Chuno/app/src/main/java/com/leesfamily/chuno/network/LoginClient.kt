package com.leesfamily.chuno.network

import android.util.Log
import com.leesfamily.chuno.network.data.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginClient {

    fun getter() {
        LoginServer.loginServer.getData("1").enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if(response.isSuccessful){
                val result = response.body()
                Log.d(TAG, "onResponse: 성공, 결과 : ${result}")

                }else{
                    Log.d(TAG, "onResponse: 실패, 코드 : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.d(TAG, "onFailure: 실패, ${t.message}")
            }
        }

        )
    }

    companion object {
        private const val TAG = "추노"
    }


}