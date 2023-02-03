package com.leesfamily.chuno.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

object PermissionHelper {

    const val CAMERA_PERMISSION = Manifest.permission.CAMERA
//    const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    const val RECORD_AUDIO = Manifest.permission.RECORD_AUDIO
    const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION

    val permissionArray = arrayOf(
        CAMERA_PERMISSION,
//        READ_EXTERNAL_STORAGE,
        RECORD_AUDIO,
        ACCESS_FINE_LOCATION,
        ACCESS_COARSE_LOCATION
    )

    fun hasCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            CAMERA_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasAudioPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

//    fun hasCallPhonePermission(context: Context): Boolean {
//        return ContextCompat.checkSelfPermission(
//            context,
//            READ_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED
//    }

    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestCameraPermission(launcher: ActivityResultLauncher<String>) {
        launcher.launch(CAMERA_PERMISSION)
    }

    fun requestAudioPermission(launcher: ActivityResultLauncher<String>) {
        launcher.launch(RECORD_AUDIO)
    }

    fun shouldShowRequestPermissionRationale(activity: Activity): Boolean {
        return activity.shouldShowRequestPermissionRationale(CAMERA_PERMISSION)
    }

    fun launchPermissionSettings(activity: Activity) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", activity.packageName, null)
        activity.startActivity(intent)
    }

}