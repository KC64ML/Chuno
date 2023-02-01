package com.leesfamily.chuno.util.custom

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.CreateRoomDialog2Binding
import com.leesfamily.chuno.util.PermissionHelper
import com.leesfamily.chuno.util.custom.DialogSizeHelper.dialogFragmentResize


class CreateRoomDialog2(
    context: Context,
    createRoomDialogInterface: CreateRoomDialogInterface
) : DialogFragment(), MyCustomDialogInterface, View.OnClickListener, OnMapReadyCallback {

    private lateinit var binding: CreateRoomDialog2Binding
    private var createRoomDialogInterface: CreateRoomDialogInterface? = null

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lastKnownLocation: Location? = null
    private val defaultLocation = LatLng(37.56, 126.97)

    var mMap: GoogleMap? = null

    private val DEFAULT_ZOOM = 14

    private var circle: Circle? = null

    var zoom = DEFAULT_ZOOM

    // 간격
    var step = 50

    // 최소
    var minValue = 300

    // 최대
    var maxValue = 1000

    // 기본값
    var defValue = 500

    // 방정보 보기 전용
    var isReadOnly: Boolean = false

    var mContext: Context? = null


    // 인터페이스 연결
    init {
        this.createRoomDialogInterface = createRoomDialogInterface
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        getDeviceLocation(requireActivity(), mContext!!)
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 0.8f)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CreateRoomDialog2Binding.inflate(inflater, container, false)

        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        binding.yesButton.setOnClickListener(this)
        binding.closeButton.setOnClickListener(this)
        binding.noButton.setOnClickListener(this)
        val roundValue = binding.roomRoundValue.apply {
            text = defValue.toString()
        }
        binding.map.apply {
            val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this@CreateRoomDialog2)
//            lifecycleScope.launchWhenCreated {
//                // Get map
//                val googleMap = mapFragment.awaitMap()
//
//                // Wait for map to finish loading
//                googleMap.awaitMapLoad()
//
//                // Ensure all places are visible in the map
//                val bounds = LatLngBounds.builder()
//                bounds.include(
//                    if (lastKnownLocation != null) {
//                        LatLng(
//                            lastKnownLocation!!.latitude,
//                            lastKnownLocation!!.longitude
//                        )
//                    } else {
//                        defaultLocation
//                    }
//                )
//                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), DEFAULT_ZOOM))
//            }
        }

        binding.roomRoundEdit.apply {
            setSeekBarMax(this, maxValue)
            setSeekBarDefault(this, maxValue)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
//                    val before = Integer.parseInt(roundValue.text.toString())
                    val curValue = setSeekBarChange(progress, roundValue)
//                    when {
//                        curValue - before > 0 -> {
//                            mMap!!.animateCamera(CameraUpdateFactory.zoomOut())
//                        }
//                        else -> {
//                            mMap!!.animateCamera(CameraUpdateFactory.zoomIn())
//                        }
//                    }

                    onAddCircle(curValue.toDouble())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        setReadOnly()
        isCancelable = false
        return binding.root
    }

    private fun setReadOnly() {
        if (isReadOnly) {
            Log.d(TAG, "setReadOnly: $isReadOnly")
            binding.titleView.text = getString(R.string.room_info_text)
            binding.roomRoundEdit.isEnabled = !isReadOnly
        }
    }

    private fun setSeekBarMax(sb: AppCompatSeekBar, max_value: Int) {
        sb.max = ((max_value - minValue) / step)
    }

    private fun setSeekBarDefault(sb: AppCompatSeekBar, max_value: Int) {
        sb.progress = sb.max / (max_value / defValue) - 1
    }

    private fun setSeekBarChange(progress: Int, tv: TextView): Int {
        val value = minValue + progress * step
        tv.text = value.toString()
        return value
    }

    private fun showCustomDialog() {
        MyCustomDialog(mContext!!, this).apply {
            message = getString(R.string.create_room_message)
            yesMsg = getString(R.string.ok)
            show()
        }
    }

    override fun onYesButtonClicked() {
    }

    override fun onNoButtonClicked() {
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.yesButton -> {
                Log.d(TAG, "onClick: 저장하여 서버와 통신")
                if (!isReadOnly)
                    showCustomDialog()
                dismiss()
            }
            binding.closeButton -> {
                dismiss()
            }
            binding.noButton -> {
                createRoomDialogInterface?.onPrevButtonClicked(binding.root)
                dismiss()
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
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

//        onAddCircle(defValue.toDouble())
    }

    //마커 , 원추가
    private fun onAddCircle(meter: Double) {
        // 반경 1KM원
        circle?.remove()
        circle = mMap!!.addCircle(
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
                .strokeColor(ContextCompat.getColor(mContext!!, R.color.blue))
                .fillColor(ContextCompat.getColor(mContext!!, R.color.blue_trans)) //배경색
        )
        mMap!!.animateCamera(
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
                            mMap!!.isMyLocationEnabled = true
                            onAddCircle(defValue.toDouble())

                        }
                    } else {
                        Log.d(
                            TAG,
                            "Current location is null. Using defaults."
                        )
                        Log.e(TAG, "Exception: %s", task.exception)
                        mMap?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                    }

                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    companion object {
        private const val TAG = "추노_CreateRoomDialog2"
    }

}
