package com.leesfamily.chuno

import android.Manifest
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.leesfamily.chuno.databinding.ActivityMainBinding
import com.leesfamily.chuno.network.data.User
import com.leesfamily.chuno.util.PermissionHelper
import com.leesfamily.chuno.util.custom.MyCustomDialog
import com.leesfamily.chuno.util.custom.MyCustomDialogInterface
import com.leesfamily.chuno.util.login.UserDB
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        binding = ActivityMainBinding.inflate(layoutInflater)


        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.loadLoginInfo()

//        viewModel.getUsers().observe(this, Observer<List<User>> { users ->
//            // update UI
//        })

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
            UserDB.isLogin() -> R.id.homeFragment
            else -> R.id.permissionFragment
        }

        navGraph.setStartDestination(startDestination)
        navController.setGraph(navGraph,null)

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