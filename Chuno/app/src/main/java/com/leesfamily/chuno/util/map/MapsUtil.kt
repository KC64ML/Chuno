/*
package com.leesfamily.chuno.util.map

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.leesfamily.chuno.R
import com.leesfamily.chuno.util.PermissionHelper
import com.leesfamily.chuno.util.custom.CreateRoomDialog2


const val TAG = "추노_MapsUtil"

object MapsUtil : OnMapReadyCallback {

    lateinit var mMap: GoogleMap
    const val DEFAULT_ZOOM = 15
    val defaultLocation = LatLng(37.56, 126.97)
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var currentPosition: LatLng

    // 원
    private var circle: Circle? = null
    var isAddCircle = false
    var circleFillColor: Int? = null
    var circleStrokeColor: Int? = null
    var circleDefValue: Double = 500.0

    fun initMapsUtil(fusedLocationProviderClient: FusedLocationProviderClient,
                     locationRequest: LocationRequest) {
        this.mFusedLocationClient = fusedLocationProviderClient
        this.locationRequest = locationRequest
    }

    fun getOnMapReadyCallback(): OnMapReadyCallback {
        return this
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setDefaultLocation()

        mMap.apply {
            uiSettings.isMyLocationButtonEnabled = false
            uiSettings.isMapToolbarEnabled = false
            uiSettings.setAllGesturesEnabled(false)

            mapType = GoogleMap.MAP_TYPE_NORMAL
//            moveCamera(
//                CameraUpdateFactory.newLatLngZoom(
//                    if (lastKnownLocation == null) {
//                        defaultLocation
//                    } else {
//                        LatLng(
//                            lastKnownLocation!!.latitude,
//                            lastKnownLocation!!.longitude
//                        )
//                    },
//                    DEFAULT_ZOOM.toFloat()
//                )
//            )

        }
        startLocationUpdates()
        if (isAddCircle)
            onAddCircle(circleDefValue, circleFillColor!!, circleStrokeColor!!)
        if (lastKnownLocation != null) {
            mMap?.isMyLocationEnabled = true
            mMap?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        lastKnownLocation!!.latitude,
                        lastKnownLocation!!.longitude
                    ), DEFAULT_ZOOM.toFloat()
                )
            )
        }
    }
    private fun startLocationUpdates() {
        // 위치서비스 활성화 여부 check
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
//            if (checkPermission()) {
                mFusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null
                )
                if (mMap != null) mMap!!.isMyLocationEnabled = true
//            }
        }
    }

    private fun setDefaultLocation() {

        //초기 위치를 서울로
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
            defaultLocation,
            DEFAULT_ZOOM.toFloat()
        )
        mMap.moveCamera(cameraUpdate)
    }

    //마커 , 원추가
    fun onAddCircle(meter: Double, fillColor: Int, strokeColor: Int) {
        // 반경 1KM원
        circle?.remove()
        circle = mMap.addCircle(
            CircleOptions().center(
                if (lastKnownLocation != null) {
                    LatLng(
                        lastKnownLocation!!.latitude,
                        lastKnownLocation!!.longitude
                    )
                } else {
                    defaultLocation
                }
            ) //원점
                .radius(meter) //반지름 단위 : m
                .strokeWidth(2f) //선너비 0f : 선없음
                .strokeColor(strokeColor)
                .fillColor(fillColor) //배경색
        )
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                circle!!.center,
                getZoomLevel(circle)
            )
        )
    }

    private fun getZoomLevel(circle: Circle?): Float {
        var zoomLevel = 0f
        if (circle != null) {
            val radius = circle.radius
            val scale = radius / 500
            zoomLevel = (DEFAULT_ZOOM - Math.log(scale) / Math.log(2.0)).toInt().toFloat()
        }
        return zoomLevel + .4f
    }

    @SuppressLint("MissingPermission")
    fun getDeviceLocation(
        activity: Activity,
        context: Context,
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        try {
            if (PermissionHelper.hasLocationPermission(context)) {
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
                            mMap?.isMyLocationEnabled = true
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        mMap?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
//                mMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun checkLocationServicesStatus(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
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
}*/
