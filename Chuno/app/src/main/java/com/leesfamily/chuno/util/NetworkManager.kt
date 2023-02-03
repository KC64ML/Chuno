package com.leesfamily.chuno.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.leesfamily.chuno.App
/**
 * 해당 클래스는 네트워크 연결을 확인하는 유틸이다.
 * isConnect의 반환값이 true면 네트워크 연결, false이면 연결되지 않음
 * */

object NetworkManager {
    fun isConnect(): Boolean {
        val context = App.context
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetwork ?: return false
        val actNw = cm.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}