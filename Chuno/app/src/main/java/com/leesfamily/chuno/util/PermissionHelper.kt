package com.leesfamily.chuno.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionHelper {
    private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    private const val CALL_PHONE_PERMISSION = Manifest.permission.CALL_PHONE
    private const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION

//    private val permissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            permissions.entries.forEach {
//                val permissionName = it.key
//                val isGranted = it.value
//
//                if (!isGranted) {
//                    if (permissionName == "android.permission.CAMERA")
//                        showPermissionDialog(
//                            getString(R.string.permission_access_message),
//                            getString(R.string.camera_permission_text),
//                            getString(R.string.go_settings),
//                            getString(R.string.cancel)
//                        )
//                    if (permissionName == "android.permission.RECORD_AUDIO")
//                        showPermissionDialog(
//                            getString(R.string.permission_access_message),
//                            getString(R.string.record_audio_permission_text),
//                            getString(R.string.go_settings),
//                            getString(R.string.cancel)
//                        )
//                }
//                if (PermissionHelper.hasAudioPermission(this) && PermissionHelper.hasCameraPermission(
//                        this
//                    )
//                )
//                    coachMark()
//
//            }
//
//        }


    fun hasGPSPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

//    private fun getLocationPermission() {
//
//        if (ContextCompat.checkSelfPermission(
//                this.applicationContext,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            )
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//            locationPermissionGranted = true
//        } else {
//            ActivityCompat.requestPermissions(
//                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
//            )
//        }
//    }

    // 앱의 Permission
    fun launchPermissionSettings(activity: Activity) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", activity.packageName, null)
        activity.startActivity(intent)
    }
}