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
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.leesfamily.chuno.util.PermissionHelper
import com.leesfamily.chuno.util.custom.CreateRoomDialog2


const val TAG = "추노_MapsUtil"

object
MapsUtil : OnMapReadyCallback {

    var lastKnownLocation: Location? = null
    val defaultLocation = LatLng(37.56, 126.97)
    var locationManager: LocationManager? = null
    var mMap: GoogleMap? = null
    const val DEFAULT_ZOOM = 15

    fun initLocationManager(locationManager: LocationManager) {
        this.locationManager = locationManager
    }

    private val listener = object : android.location.LocationListener {
        //위치가 변경될때 호출될 method

        override fun onLocationChanged(location: Location) {
            when (location.provider) {
                LocationManager.GPS_PROVIDER -> {

                    Log.d("$TAG GPS : ", "${location.latitude}/${location.longitude}")
                }
                LocationManager.NETWORK_PROVIDER -> {
                    Log.d("$TAG NETWORK : ", "${location.latitude}/${location.longitude}")
                }
                LocationManager.PASSIVE_PROVIDER -> {
                    Log.d("$TAG PASSIVE : ", "${location.latitude}/${location.longitude}")
                }

            }
        }

        override fun onLocationChanged(locations: MutableList<Location>) {
            super.onLocationChanged(locations)
            locations.forEach { location ->
                when (location.provider) {
                    LocationManager.GPS_PROVIDER -> {
                        Log.d("$TAG GPS : ", "${location.latitude}/${location.longitude}")
                    }
                    LocationManager.NETWORK_PROVIDER -> {
                        Log.d("$TAG NETWORK : ", "${location.latitude}/${location.longitude}")
                    }
                    LocationManager.PASSIVE_PROVIDER -> {
                        Log.d("$TAG PASSIVE : ", "${location.latitude}/${location.longitude}")
                    }

                }
            }

        }

        @SuppressLint("MissingPermission")
        override fun onProviderEnabled(provider: String) {
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
            locationManager?.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0f, this)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "onMapReady: ")
        mMap = googleMap

        mMap!!.apply {
            uiSettings.isMyLocationButtonEnabled = false
            uiSettings.isMapToolbarEnabled = false

            uiSettings.setAllGesturesEnabled(false)
            mapType = GoogleMap.MAP_TYPE_NORMAL
            moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    if (lastKnownLocation == null) {
                        defaultLocation
                    } else {
                        LatLng(
                            lastKnownLocation!!.latitude,
                            lastKnownLocation!!.longitude
                        )
                    },
                    DEFAULT_ZOOM.toFloat()
                )
            )

        }
    }

    //마커 , 원추가
    fun onAddMarker(radius: Double, strokeWidth: Float, color: Int) {
        // 반경 1KM원
        val circle1KM = CircleOptions().center(defaultLocation) //원점
            .radius(radius) //반지름 단위 : m
            .strokeWidth(strokeWidth) //선너비 0f : 선없음
            .fillColor(color) //배경색

        //원추가
        mMap!!.addCircle(circle1KM)
    }

    @SuppressLint("MissingPermission")
    fun getDeviceLocation(
        activity: Activity,
        context: Context,
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
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

    @SuppressLint("MissingPermission")
    private fun getProviders() {
        val listProviders = locationManager?.allProviders as MutableList<String>
        for (provider in listProviders) {
            when (provider) {
                LocationManager.GPS_PROVIDER -> {
                    locationManager!!.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0L,
                        0f,
                        listener
                    )
                }
                LocationManager.NETWORK_PROVIDER -> {
                    locationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        0,
                        0f,
                        listener
                    )
                }
                LocationManager.PASSIVE_PROVIDER -> {
                    locationManager!!.requestLocationUpdates(
                        LocationManager.PASSIVE_PROVIDER,
                        0,
                        0f,
                        listener
                    )
                }

                LocationManager.FUSED_PROVIDER -> {
                    locationManager!!.requestLocationUpdates(
                        LocationManager.FUSED_PROVIDER,
                        0,
                        0f,
                        listener
                    )
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun setLastLocation(fragment: Fragment) {
        //GPS provider
        lastKnownLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (lastKnownLocation != null) {
            Log.d(
                TAG,
                "latitude=${lastKnownLocation!!.latitude}, longitude=${lastKnownLocation!!.longitude}"
            )
        }
        //network provider
        lastKnownLocation = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (lastKnownLocation != null) {
            Log.d(
                TAG,
                "latitude=${lastKnownLocation!!.latitude}, longitude=${lastKnownLocation!!.longitude}"
            )
        }
        //passive provider
        lastKnownLocation = locationManager?.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
        if (lastKnownLocation != null) {
            Log.d(
                TAG,
                "latitude=${lastKnownLocation!!.latitude}, longitude=${lastKnownLocation!!.longitude}"
            )
        }

        //fused provider
        lastKnownLocation = locationManager?.getLastKnownLocation(LocationManager.FUSED_PROVIDER)
        if (lastKnownLocation != null) {
            Log.d(
                TAG,
                "latitude=${lastKnownLocation!!.latitude}, longitude=${lastKnownLocation!!.longitude}"
            )
        }
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