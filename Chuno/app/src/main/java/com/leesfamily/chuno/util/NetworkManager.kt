package com.leesfamily.chuno.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.leesfamily.chuno.App
import com.leesfamily.chuno.R
import com.leesfamily.chuno.util.custom.MyCustomDialog
import com.leesfamily.chuno.util.custom.MyCustomDialogInterface
import kotlin.system.exitProcess

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

    fun isConnectNetwork(context: Context): Boolean {
        if (isConnect())
            return true

        MyCustomDialog(context, object : MyCustomDialogInterface {
            override fun onYesButtonClicked() {
                exitProcess(0)
            }

            override fun onNoButtonClicked() {
            }
        }).apply {
            message = context.getString(R.string.no_internet_connection)
            yesMsg = context.getString(R.string.ok)
            show()
        }
        return false
    }
}