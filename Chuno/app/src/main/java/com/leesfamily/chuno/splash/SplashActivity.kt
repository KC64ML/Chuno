package com.leesfamily.chuno.splash

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.leesfamily.chuno.MainActivity
import com.leesfamily.chuno.MainViewModel
import com.leesfamily.chuno.R
import com.leesfamily.chuno.network.LoginGetter
import com.leesfamily.chuno.util.NetworkManager
import com.leesfamily.chuno.util.custom.MyCustomDialog
import com.leesfamily.chuno.util.custom.MyCustomDialogInterface
import com.leesfamily.chuno.util.login.LoginPrefManager
import com.leesfamily.chuno.util.login.UserDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // 액션바 숨기기

        setContentView(R.layout.activity_splash)

        lifecycleScope.launch(Dispatchers.IO) {
            val start = System.currentTimeMillis()
//            viewModelInit()
            val delay = System.currentTimeMillis() - start

            val limitTime = 300

            if (delay < limitTime)
                delay(limitTime - delay)
            if (isConnectNetwork())
                LoginPrefManager.getLastLoginToken()?.let {
                    LoginGetter().requestUser(it)?.let { user ->
                        UserDB.setUser(user)
                        UserDB.setToken(it)
                    }
                }

            launch(Dispatchers.Main) {
                startMainActivity()
            }
        }
    }

    private fun isConnectNetwork(): Boolean {
        if (NetworkManager.isConnect())
            return true

        MyCustomDialog(applicationContext, object : MyCustomDialogInterface {
            override fun onYesButtonClicked() {
                exitProcess(0)
            }

            override fun onNoButtonClicked() {
            }
        }).apply {
            message = getString(R.string.no_internet_connection)
            yesMsg = getString(R.string.ok)
            show()
        }
        return false
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}