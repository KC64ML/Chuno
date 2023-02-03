package com.leesfamily.chuno.network

import android.util.Log
import com.leesfamily.chuno.network.data.DataForm
import com.leesfamily.chuno.network.data.User
import com.leesfamily.chuno.util.login.UserDB
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Response


class LoginGetter {
    private var data: DataForm? = null

    fun requestUser(token: String): User? {

        val userResponse: Response<DataForm> = ChunoServer.loginServer.getUserData(token).execute()

        val networkResponse = userResponse.raw().networkResponse?.code
        val requestCode = userResponse.code()
        Log.d(TAG, "requestLogin: loginResponse\n $userResponse")
        Log.d(TAG, "requestLogin: requestCode\n $requestCode")
        Log.d(TAG, "requestLogin: networkResponse\n $networkResponse")
        if (requestCode == 200) {
            data = userResponse.body()
            val user: User = data?.result as User

            Log.d(TAG, "requestLogin: request success, $user")
            return user
        }
        return null
    }

    fun requestLogin(token: String): String? {

        val loginResponse: Response<DataForm> = ChunoServer.loginServer.login(token).execute()

        val networkResponse = loginResponse.raw().networkResponse?.code
        val requestCode = loginResponse.code()
        Log.d(TAG, "requestLogin: loginResponse\n $loginResponse")
        Log.d(TAG, "requestLogin: requestCode\n $requestCode")
        Log.d(TAG, "requestLogin: networkResponse\n $networkResponse")
        if (requestCode == 200) {
            data = loginResponse.body()

            when (data?.code) {
                "member" -> {
                    UserDB.setToken(data!!.result.toString())
                }
                "no_email" -> {
                    UserDB.setEmail(data!!.result.toString())
                }
            }
            Log.d(TAG, "requestLogin: request success, $data")
        }
        return data?.code.toString()
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

    companion object {
        private const val TAG = "추노_LoginGetter"
    }
}