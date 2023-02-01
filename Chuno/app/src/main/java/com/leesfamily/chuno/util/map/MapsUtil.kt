package com.leesfamily.chuno.util.map

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.leesfamily.chuno.util.PermissionHelper


const val TAG = "추노_MapsUtil"

object MapsUtil : OnMapReadyCallback, LocationListener {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lastKnownLocation: Location? = null
    private val defaultLocation = LatLng(37.56, 126.97)
    var mMap: GoogleMap? = null
    private const val DEFAULT_ZOOM = 15

    val options = GoogleMapOptions().apply {
        compassEnabled(false)
        mapToolbarEnabled(false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap!!.apply {
//            uiSettings.isMyLocationButtonEnabled = true
            uiSettings.setAllGesturesEnabled(false)
            moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
        }

        onAddMarker()
    }

    //마커 , 원추가
    fun onAddMarker() {

        // 반경 1KM원
        val circle1KM = CircleOptions().center(defaultLocation) //원점
            .radius(1000.0) //반지름 단위 : m
            .strokeWidth(1f) //선너비 0f : 선없음
            .fillColor(Color.parseColor("#880000ff")) //배경색

        //원추가
        mMap!!.addCircle(circle1KM)
    }

    @SuppressLint("MissingPermission")
    fun getDeviceLocation(activity: Activity, context: Context) {
        try {
            if (PermissionHelper.hasGPSPermission(context)) {
                val locationResult = fusedLocationProviderClient.lastLocation

                locationResult.addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            mMap?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        mMap?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                        mMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    override fun onLocationChanged(location: Location) {

    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private fun showDialogForLocationServiceSetting(activity: Activity, context: Context) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage(
            """
            앱을 사용하기 위해서는 위치 서비스가 필요합니다.
            위치 설정을 해주세요.
            """.trimIndent()
        )
        builder.setCancelable(true)
        builder.setPositiveButton("설정", DialogInterface.OnClickListener { dialog, id ->
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            activity.startActivity(callGPSSettingIntent)
        })
        builder.setNegativeButton("취소",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        builder.create().show()
    }
}