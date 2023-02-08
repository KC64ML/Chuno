package com.leesfamily.chuno.network.login

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.leesfamily.chuno.MainViewModel
import com.leesfamily.chuno.network.ChunoServer
import com.leesfamily.chuno.network.data.*
import com.leesfamily.chuno.util.FormDataUtil
import com.leesfamily.chuno.util.login.LoginPrefManager
import com.leesfamily.chuno.util.login.UserDB
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File


class LoginGetter {
    private var loginData: LoginForm? = null
    private var userData: DataForm? = null

    // JWT를 주고 User 정보를 받아오는 method
    fun requestUser(token: String): User? {

        val userResponse: Response<DataForm> = ChunoServer.loginServer.getUserData(token).execute()

        val networkResponse = userResponse.raw().networkResponse?.code
        val requestCode = userResponse.code()
        Log.d(TAG, "requestUser: loginResponse\n $userResponse")
        Log.d(TAG, "requestUser: requestCode\n $requestCode")
        Log.d(TAG, "requestUser: networkResponse\n $networkResponse")
        Log.d(TAG, "requestUser: token\n $token")
        if (requestCode == 200) {
            userData = userResponse.body()
            val user: User? = userData?.result

            Log.d(TAG, "requestLogin: request success, $user")
            return user
        }
        return null
    }

    // kakao 토큰을 주고 JWT를 받아오는 method
    // member일 경우 code = "memeber", result = token
    // no_email일 경우 code = "no_email", result = email
    fun requestLogin(token: String): LoginForm? {

        val loginResponse: Response<LoginForm> = ChunoServer.loginServer.login(token).execute()
        val networkResponse = loginResponse.raw().networkResponse?.code
        val requestCode = loginResponse.code()
        Log.d(TAG, "requestLogin: loginResponse\n $loginResponse")
        Log.d(TAG, "requestLogin: requestCode\n $requestCode")
        Log.d(TAG, "requestLogin: networkResponse\n $networkResponse")
        if (requestCode == 200) {
            loginData = loginResponse.body()
            Log.d(TAG, "requestLogin: loginData $loginData")
        }
        return loginData
    }

    // JWT의 유효성 검사하는 method
    // return : Int, 0일 경우 유효하지 않음, 1일 경우 유효함
    fun requestTokenConfirm(token: String): Boolean {

        Log.d(TAG, "requestTokenConfirm: token $token")

        val body: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), token)

        val loginResponse: Response<Int> = ChunoServer.loginServer.tokenConfirm(body).execute()

        val networkResponse = loginResponse.raw().networkResponse?.code
        val requestCode = loginResponse.code()
        Log.d(TAG, "requestTokenConfirm: loginResponse\n $loginResponse")
        Log.d(TAG, "requestTokenConfirm: requestCode\n $requestCode")
        Log.d(TAG, "requestTokenConfirm: networkResponse\n $networkResponse")
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

    // 닉네임 중복확인 method
    fun requestNickDuplic(nick: String): Boolean {

        val loginResponse: Response<NickForm> =
            ChunoServer.loginServer.getNickDuplic(nick).execute()

        val networkResponse = loginResponse.raw().networkResponse?.code
        val requestCode = loginResponse.code()
        Log.d(TAG, "requestNickDuplic: loginResponse\n $loginResponse")
        Log.d(TAG, "requestNickDuplic: requestCode\n $requestCode")
        Log.d(TAG, "requestNickDuplic: networkResponse\n $networkResponse")
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


    fun requestRegisterUser(nickname: String, email: String, phone: String, image: File?): String? {

        val formNickName = FormDataUtil.getBody("nickname", nickname)
        val formEmail = FormDataUtil.getBody("email", email)
        val formPhone = FormDataUtil.getBody("phone", phone)
        val formFile = image?.let { FormDataUtil.getImageBody("file", it) }

        val loginResponse: Response<String> =
            ChunoServer.loginServer.registerUser(formNickName, formEmail, formPhone, formFile).execute()

        val networkResponse = loginResponse.raw().networkResponse?.code
        val requestCode = loginResponse.code()
        Log.d(TAG, "requestRegisterUser: loginResponse\n $loginResponse")
        Log.d(TAG, "requestRegisterUser: requestCode\n $requestCode")
        Log.d(TAG, "requestRegisterUser: networkResponse\n $networkResponse")
        if (requestCode == 200) {
            val token = loginResponse.body()
            Log.d(TAG, "requestRegisterUser: token $token")
            return token
        }
        return null
    }

    fun requestRegisterImage(token: String, nickname: String, phone:String,image: File?): Boolean? {

        val formNickName = FormDataUtil.getBody("nickname", nickname)
        val formPhone = FormDataUtil.getBody("phone", phone)
        val formFile = image?.let { FormDataUtil.getImageBody("file", it) }

        val loginResponse: Response<DataForm> =
            ChunoServer.loginServer.profileImage(token, formNickName, formPhone, formFile).execute()

        val networkResponse = loginResponse.raw().networkResponse?.code
        val requestCode = loginResponse.code()
        Log.d(TAG, "requestRegisterImage: loginResponse\n $loginResponse")
        Log.d(TAG, "requestRegisterImage: requestCode\n $requestCode")
        Log.d(TAG, "requestRegisterImage: networkResponse\n $networkResponse")
        if (requestCode == 200) {
            val result = loginResponse.body()
            when (result?.code) {
                0 -> {
                    return false
                }
                1 -> {
                    return true
                }
            }
            Log.d(TAG, "requestRegisterImage: token $result")
        }
        return null
    }

    fun requestDeleteUser(token: String): Boolean? {

        val userResponse: Response<DataForm> = ChunoServer.loginServer.deleteUser(token).execute()

        val networkResponse = userResponse.raw().networkResponse?.code
        val requestCode = userResponse.code()
        Log.d(TAG, "requestDeleteUser: loginResponse\n $userResponse")
        Log.d(TAG, "requestDeleteUser: requestCode\n $requestCode")
        Log.d(TAG, "requestDeleteUser: networkResponse\n $networkResponse")
        Log.d(TAG, "requestDeleteUser: token\n $token")
        if (requestCode == 200) {
            val code = userResponse.body()?.code
            Log.d(TAG, "requestDeleteUser: code $code")
            when (userResponse.body()?.code) {
                0 -> {
                    return false
                }
                1 -> {
                    return true
                }
            }
            Log.d(TAG, "requestDeleteUser: request success, ${code}")
        }
        return null
    }

    companion object {
        private const val TAG = "추노_LoginGetter"
    }
}