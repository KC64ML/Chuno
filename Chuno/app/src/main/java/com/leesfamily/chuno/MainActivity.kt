package com.leesfamily.chuno

import android.app.Fragment
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentOnAttachListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
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
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        binding = ActivityMainBinding.inflate(layoutInflater)

        if (NetworkManager.isConnectNetwork(this@MainActivity)) {
            viewModel.setItemList()
            viewModel.setUserInfo()
        }
        supportFragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->
            Log.d(TAG, "addFragmentOnAttachListener: ")
            if (UserDB.getIsConfirmToken())
                LoginPrefManager.getLastLoginToken()?.let {
                    viewModel.setLastToken(it)
                }
            else {
                navigate()
            }
        }

        navHostFragment =
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
                Log.d(TAG, "onCreate: mainActivity :viewModel.setToken(it) $lastToken")
                R.id.homeFragment
            }
            else -> R.id.permissionFragment
        }

        navGraph.setStartDestination(startDestination)
        navController.setGraph(navGraph, null)

        setContentView(binding.root)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
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

    fun navigate() {
        navHostFragment.findNavController().navigate(R.id.loginFragment)
    }

    companion object {
        private const val TAG = "추노_Main"
    }
}