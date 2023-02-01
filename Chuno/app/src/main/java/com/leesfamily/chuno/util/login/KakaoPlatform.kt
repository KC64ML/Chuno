package com.leesfamily.chuno.util.login

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.leesfamily.chuno.App

class KakaoPlatform {
//    private lateinit var listener: LoginListener

    companion object {
        private const val TAG = "추노_카카오로그인"
    }

    fun login(fragment: Fragment) {
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공, 토큰 : ${token.accessToken}")

                getUserData()
            }
        }
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(fragment.requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(fragment.requireContext()) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(
                        fragment.requireContext(),
                        callback = callback
                    )
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ,토큰 : ${token.accessToken}")
//                    listener.loginSuccess()
//                    App.prefs.setString("login_access_token", token.accessToken)
//                    getUserData()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                fragment.requireContext(),
                callback = callback
            )
        }
    }
    fun getUserData(){
        UserApiClient.instance.me { user, error ->
            if (user == null) {
//                listener.loginFailed()
                return@me
            }
            user.kakaoAccount?.email?.let {
                UserDB.setEmail(it)
            Log.d(TAG, "getUserData: 이메일 : $it")
            }
//            listener.loginSuccess()
        }
    }

    fun logout(fragment: Fragment) {
        // 로그아웃
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            } else {
                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                Toast.makeText(
                    fragment.requireContext(),
                    "로그아웃 성공. SDK에서 토큰 삭제됨",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun unlink(fragment: Fragment) {
        // 연결 끊기
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e(TAG, "연결 끊기 실패", error)
            } else {
                Log.i(TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                Toast.makeText(
                    fragment.requireContext(),
                    "연결 끊기 성공. SDK에서 토큰 삭제 됨",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}