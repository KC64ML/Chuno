package com.leesfamily.chuno.util.login

import androidx.fragment.app.Fragment
import com.leesfamily.chuno.network.login.LoginGetter

object LoginManager {
    private lateinit var listener: LoginListener

    private val kakaoPlatform by lazy {
        KakaoPlatform()
    }

    fun setListener(listener: LoginListener) {
        this.listener = listener
    }

    // 로그인
    fun login(fragment: Fragment) {
        kakaoPlatform.setListener(listener)
        kakaoPlatform.login(fragment)
    }

    suspend fun confirmLogin(token:String): Boolean{
        return LoginGetter().requestTokenConfirm(token)
    }

    // 로그아웃
    fun logout(fragment: Fragment) {
        kakaoPlatform.logout(fragment)
    }

    // 탈퇴
    fun unlink(fragment: Fragment) {
        kakaoPlatform.unlink(fragment)
    }
}