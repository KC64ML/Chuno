package com.leesfamily.chuno.util.login

import androidx.fragment.app.Fragment

object LoginManager {
    private lateinit var listener: LoginListener

    private val kakaoPlatform by lazy{
        KakaoPlatform()
    }

    // 로그인
    fun login(fragment: Fragment){
        kakaoPlatform.login(fragment)
    }

    // 로그아웃
     fun logout(fragment: Fragment) {
        kakaoPlatform.logout(fragment)
    }

    // 탈퇴
     fun unlink(fragment: Fragment) {
       kakaoPlatform.unlink(fragment)
    }

    fun isLogin(): Boolean {
        return UserDB.isLogin()
    }
}