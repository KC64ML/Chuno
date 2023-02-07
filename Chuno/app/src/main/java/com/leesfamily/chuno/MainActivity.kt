package com.leesfamily.chuno

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.leesfamily.chuno.databinding.ActivityMainBinding
import com.leesfamily.chuno.network.data.Item
import com.leesfamily.chuno.network.data.User
import com.leesfamily.chuno.network.item.ItemGetter
import com.leesfamily.chuno.network.login.LoginGetter
import com.leesfamily.chuno.util.NetworkManager
import com.leesfamily.chuno.util.PermissionHelper
import com.leesfamily.chuno.util.login.LoginPrefManager
import com.leesfamily.chuno.util.login.UserDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        binding = ActivityMainBinding.inflate(layoutInflater)

        if (NetworkManager.isConnectNetwork(this@MainActivity)) {

            viewModel.setItemList()
            viewModel.setUserInfo()
        }


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.start_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val graphInflater = navHostFragment.navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.start_nav_graph)

        val startDestination = when {
            (PermissionHelper.hasAudioPermission(this) && PermissionHelper.hasCameraPermission(
                this
            ) &&
                    /*PermissionHelper.hasCallPhonePermission(this) &&*/ PermissionHelper.hasLocationPermission(
                this
            )) -> {
                R.id.loginFragment
            }
            UserDB.getIsConfirmToken() -> {
                val lastToken =
                    LoginPrefManager.getLastLoginToken()
                viewModel.setLastToken(lastToken!!)
                Log.d(TAG, "onCreate: mainactivity :viewModel.setToken(it) $lastToken")
                R.id.homeFragment
            }
            else -> R.id.permissionFragment
        }

        navGraph.setStartDestination(startDestination)
        navController.setGraph(navGraph, null)

        setContentView(binding.root)
    }


    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    companion object {
        private const val TAG = "추노_Main"
    }
}