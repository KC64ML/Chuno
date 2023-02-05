package com.leesfamily.chuno.network

import android.util.Log
import com.google.gson.Gson
import com.leesfamily.chuno.network.data.DataForm
import com.leesfamily.chuno.network.data.LoginForm
import com.leesfamily.chuno.network.data.NickForm
import com.leesfamily.chuno.network.data.User
import com.leesfamily.chuno.util.login.LoginPrefManager
import com.leesfamily.chuno.util.login.UserDB
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Response


class LoginGetter {
    private var loginData: LoginForm? = null
    private var userData: DataForm? = null

    fun requestUser(token: String): User? {

        val userResponse: Response<DataForm> = ChunoServer.loginServer.getUserData(token).execute()

        val networkResponse = userResponse.raw().networkResponse?.code
        val requestCode = userResponse.code()
        Log.d(TAG, "requestLogin: loginResponse\n $userResponse")
        Log.d(TAG, "requestLogin: requestCode\n $requestCode")
        Log.d(TAG, "requestLogin: networkResponse\n $networkResponse")
        Log.d(TAG, "requestLogin: token\n $token")
        if (requestCode == 200) {
            userData = userResponse.body()
            val user: User? = userData?.result

            Log.d(TAG, "requestLogin: request success, $user")
            return user
        }
        return null
    }

    //array 를 json String 으로 변환
    fun <T> arrayToString(list: ArrayList<T>?): String? {
        val g = Gson()
        return g.toJson(list)
    }

    fun requestLogin(token: String): String? {

        val loginResponse: Response<LoginForm> = ChunoServer.loginServer.login(token).execute()

        val networkResponse = loginResponse.raw().networkResponse?.code
        val requestCode = loginResponse.code()
        Log.d(TAG, "requestLogin: loginResponse\n $loginResponse")
        Log.d(TAG, "requestLogin: requestCode\n $requestCode")
        Log.d(TAG, "requestLogin: networkResponse\n $networkResponse")
        if (requestCode == 200) {
            loginData = loginResponse.body()

            when (loginData?.code) {
                "member" -> {
                    UserDB.setToken(loginData!!.result)
                    LoginPrefManager.setLastLoginToken(loginData!!.result)
                }
                "no_email" -> {
                    UserDB.setEmail(loginData!!.result)
                }
            }
            Log.d(TAG, "requestLogin: request success, $loginData")
        }
        return loginData?.code.toString()
    }

    fun requestTokenConfirm(token: String): Boolean {

        Log.d(TAG, "requestTokenConfirm: token $token")

        val body: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), token)

        val loginResponse: Response<Int> = ChunoServer.loginServer.tokenConfirm(body).execute()

        val networkResponse = loginResponse.raw().networkResponse?.code
        val requestCode = loginResponse.code()
        Log.d(TAG, "requestLogin: loginResponse\n $loginResponse")
        Log.d(TAG, "requestLogin: requestCode\n $requestCode")
        Log.d(TAG, "requestLogin: networkResponse\n $networkResponse")
        if (requestCode == 200) {
            Log.d(TAG, "requestTokenConfirm: loginResponse.body() : ${loginResponse.body()}")
            when (loginResponse.body()) {
                0 -> {
                    Log.d(TAG, "requestTokenConfirm: 유효하지 않은 토큰")
                    return false
                }
                1 -> {
                    Log.d(TAG, "requestTokenConfirm: 유효한 토큰")
                    return true
                }
            }
        }
        return false
    }

    fun requestNickDuplic(nick: String): Boolean {

        val loginResponse: Response<NickForm> =
            ChunoServer.loginServer.getNickDuplic(nick).execute()

        val networkResponse = loginResponse.raw().networkResponse?.code
        val requestCode = loginResponse.code()
        Log.d(TAG, "requestLogin: loginResponse\n $loginResponse")
        Log.d(TAG, "requestLogin: requestCode\n $requestCode")
        Log.d(TAG, "requestLogin: networkResponse\n $networkResponse")
        if (requestCode == 200) {
            Log.d(TAG, "requestTokenConfirm: loginResponse.body() : ${loginResponse.body()}")
            val check = loginResponse.body()?.code
            when (check) {
                0 -> {
                    Log.d(TAG, "nick: 중복되지 않은 닉네임")
                    return true
                }
                1 -> {
                    Log.d(TAG, "nick: 중복된 닉네임")
                    return false
                }
            }
        }
        return false
    }

    companion object {
        private const val TAG = "추노_LoginGetter"
    }
}