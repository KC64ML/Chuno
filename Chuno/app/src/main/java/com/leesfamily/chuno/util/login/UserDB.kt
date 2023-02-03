package com.leesfamily.chuno.util.login

import com.leesfamily.chuno.network.data.User


object UserDB {

    private var token: String? = null
    private var user: User? = null
//
//    private var id: Long? = null
//
//    private var email: String? = null
//
//    private var nickname: String? = null
//
//    private var level = 0
//
//    private var paperCount = 0
//
//    private var runnerPlayCount = 0
//
//    private var runnerWinCount = 0
//
//    private var chaserPlayCount = 0
//
//    private var chaserWinCount = 0
//
//    private var exp = 0
//
//    private var isManager = false
//
//    private var money = 0
//
//    private var profile: UserProfile? = null


    fun setEmail(email: String) {
        this.user?.email = email
    }

    fun setUser(user: User?) {
        this.user = user
//        when (user?.code) {
//            "member" -> {
//                token = user.result
//            }
//            "no_email" -> {
//                email = user.result
//            }
//        }
//        if (token == null) {
////            LoginPrefManager.removeLastLoginToken()
//            Log.d("추노_UserDB", "setUser: token null")
//        } else {
//            LoginPrefManager.setLastLoginToken(token!!)
//        }
    }

    fun getEmail(): String? {
        return user?.email
    }

    fun setToken(token: String) {
        this.token = token
    }

    fun getToken() = token

    fun isLogin(): Boolean {
        return token != null
    }
}