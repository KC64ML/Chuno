package com.leesfamily.chuno.util.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
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
import com.google.android.gms.maps.SupportMapFragment
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.CreateRoomDialog2Binding
import com.leesfamily.chuno.util.custom.DialogSizeHelper.dialogFragmentResize


class CreateRoomDialog2(
    context: Context,
    createRoomDialogInterface: CreateRoomDialogInterface
) : DialogFragment(), MyCustomDialogInterface, View.OnClickListener {

    private lateinit var binding: CreateRoomDialog2Binding
    private var createRoomDialogInterface: CreateRoomDialogInterface? = null
    var mContext: Context? = null

    private val locationManager by lazy {
        activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private val blue by lazy {
        ContextCompat.getColor(mContext!!, R.color.blue)
    }
    private val blueTrans by lazy {
        ContextCompat.getColor(mContext!!, R.color.blue_trans)
    }
//    private var circle: Circle? = null

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

    // 인터페이스 연결
    init {
        this.createRoomDialogInterface = createRoomDialogInterface
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
//        MapsUtil.initLocationManager(locationManager)
//        if (PermissionHelper.hasLocationPermission(requireContext()))
//            MapsUtil.initView()
//        MapsUtil.apply {
//            isAddCircle = true
//            circleStrokeColor = blue
//            circleFillColor = blueTrans
//        }
//        getDeviceLocation(requireActivity(), mContext!!)
//        onAddCircle(curValue.toDouble())
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        context?.dialogFragmentResize(this, 0.8f)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CreateRoomDialog2Binding.inflate(inflater, container, false)
        Log.d(TAG, "onCreateView: ")
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
//            mapFragment.getMapAsync()
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
                    val curValue = setSeekBarChange(progress, roundValue)

//                    MapsUtil.onAddCircle(curValue.toDouble(),blueTrans,blue)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

//        MapsUtil.onAddCircle(defValue.toDouble(),blue,blueTrans)
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

    /*   @SuppressLint("MissingPermission")
       override fun onMapReady(googleMap: GoogleMap) {
           Log.d(TAG, "onMapReady: ")
           MapsUtil.mMap = googleMap


           MapsUtil.mMap!!.apply {
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
       }*/

//    //마커 , 원추가
//    private fun onAddCircle(meter: Double) {
//        Log.d(TAG, "onAddCircle: ")
//        Log.d(TAG, "onAddCircle: $lastKnownLocation")
//        // 반경 1KM원
//        circle?.remove()
//        circle = MapsUtil.mMap!!.addCircle(
//            CircleOptions().center(
//                if (lastKnownLocation != null) {
//                    LatLng(
//                        lastKnownLocation!!.latitude,
//                        lastKnownLocation!!.longitude
//                    )
//                } else {
//                    defaultLocation
//                }
//            ) //원점
//                .radius(meter) //반지름 단위 : m
//                .strokeWidth(2f) //선너비 0f : 선없음
//                .strokeColor(ContextCompat.getColor(mContext!!, R.color.blue))
//                .fillColor(ContextCompat.getColor(mContext!!, R.color.blue_trans)) //배경색
//        )
//        MapsUtil.mMap!!.animateCamera(
//            CameraUpdateFactory.newLatLngZoom(
//                circle!!.center,
//                getZoomLevel(circle)
//            )
//        )
//    }
//
//    private fun getZoomLevel(circle: Circle?): Float {
//        var zoomLevel = 0f
//        if (circle != null) {
//            val radius = circle.radius
//            val scale = radius / 500
//            zoomLevel = (DEFAULT_ZOOM - Math.log(scale) / Math.log(2.0)).toInt().toFloat()
//        }
//        return zoomLevel + .4f
//    }
//
//    @SuppressLint("MissingPermission")
//    fun getDeviceLocation(activity: Activity, context: Context) {
//        try {
//            if (PermissionHelper.hasLocationPermission(context)) {
//                val locationResult = fusedLocationProviderClient.lastLocation
//
//                locationResult.addOnCompleteListener(activity) { task ->
//                    if (task.isSuccessful) {
//                        Log.d(TAG, "getDeviceLocation: ")
//                        // Set the map's camera position to the current location of the device.
//                        lastKnownLocation = task.result
//                        if (lastKnownLocation != null) {
//                            mMap?.moveCamera(
//                                CameraUpdateFactory.newLatLngZoom(
//                                    LatLng( // 이전으로 돌아갈 때, java.lang.NullPointerException
//                                        lastKnownLocation!!.latitude,
//                                        lastKnownLocation!!.latitude,
//                                        lastKnownLocation!!.longitude
//                                    ), DEFAULT_ZOOM.toFloat()
//                                )
//                            )
//                            mMap!!.isMyLocationEnabled = true
//                            onAddCircle(defValue.toDouble())
//
//                        }
//                    } else {
//                        Log.d(TAG, "Current location is null. Using defaults.")
//                        Log.e(TAG, "Exception: %s", task.exception)
//                        mMap?.moveCamera(
//                            CameraUpdateFactory
//                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
//                        )
//                    }
//
//                }
//            }
//        } catch (e: SecurityException) {
//            Log.e("Exception: %s", e.message, e)
//        }
//    }

    companion object {
        private const val TAG = "추노_CreateRoomDialog2"
    }

}
