package com.leesfamily.chuno.game

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.FragmentGameViewBinding
import com.leesfamily.chuno.openvidu.openvidu.LocalParticipant
import com.leesfamily.chuno.openvidu.openvidu.RemoteParticipant
import com.leesfamily.chuno.openvidu.openvidu.Session
import com.leesfamily.chuno.openvidu.utils.CustomHttpClient
import com.leesfamily.chuno.openvidu.websocket.CustomWebSocket
import com.leesfamily.chuno.room.shop.ShopFragment
import com.leesfamily.chuno.util.PermissionHelper
import com.leesfamily.chuno.util.custom.MyCustomDialog
import com.leesfamily.chuno.util.custom.MyCustomDialogInterface
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.webrtc.EglBase
import org.webrtc.MediaStream
import org.webrtc.SurfaceViewRenderer
import java.io.IOException
import java.util.*

class GameViewFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentGameViewBinding
    private var mMap: GoogleMap? = null
    private val mFusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }
    private val locationRequest: LocationRequest by lazy {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL).apply {
            setMinUpdateDistanceMeters(MIN_UPDATE_DISTANCE)
            setMinUpdateIntervalMillis(MIN_UPDATE_INTERVAL)
        }.build()
    }
    private lateinit var currentPosition: LatLng
    private val userMarkers: MutableList<Marker?> by lazy {
        mutableListOf()
    }

    private lateinit var callback: OnBackPressedCallback
    private var APPLICATION_SERVER_URL: String? = null
    private var session: Session? = null
    private var httpClient: CustomHttpClient? = null
    private var needRequest = false

    private var isMenu = false
    private var isMyVideo = false

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value

                if (!isGranted) {
                    if (permissionName == PermissionHelper.ACCESS_COARSE_LOCATION || permissionName == PermissionHelper.ACCESS_FINE_LOCATION)
                        showPermissionDialog(
                            getString(R.string.location_permission_message),
                            getString(R.string.setting),
                            getString(R.string.cancel)
                        )
                }

            }
        }

    //위치정보 요청시 호출
    var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val locationList = locationResult.locations
            if (locationList.size > 0) {
                val location = locationList[locationList.size - 1]
                val marker = mutableListOf(
                    LatLng(36.102208, 128.424187),
                    LatLng(36.102290, 128.423861), LatLng(36.102818, 128.424430),
                    LatLng(36.102421, 128.424620), LatLng(36.103249, 128.423868)
                )
                currentPosition = LatLng(location.latitude, location.longitude)

                Log.d(
                    TAG,
                    "onLocationResult: 위도: ${location.latitude.toString()}, 경도: ${location.longitude}"
                )

                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(location)
                addMarkerUser(marker)
            }
        }
    }

    fun addMarkerUser(marker: MutableList<LatLng>) {
        marker.forEachIndexed { index, latLng ->
            val userLatLng = LatLng(latLng.latitude, latLng.longitude)

            val markerOptions = MarkerOptions()
            markerOptions.position(userLatLng)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

            if (userMarkers.size <= index) {
                userMarkers.add(index, mMap!!.addMarker(markerOptions))
            } else {
                userMarkers[index] = mMap!!.addMarker(markerOptions)
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameViewBinding.inflate(inflater, container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        val random = Random()
        val randomIndex = random.nextInt(100)
        binding.participantName.text = binding.participantName.text.append(randomIndex.toString())
        binding.startFinishCall.setOnClickListener {
            Log.d(TAG, "onCreateView: startFinishCall clicked")
            buttonPressed(it)
        }
        val pagerAdapter = GameViewFragmentAdapter(this)
        val pager = binding.pager.apply {
            adapter = pagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Log.d(TAG, "onPageScrollStateChanged: $position")
                    if (position == 0) {
                        binding.arrowLeft.visibility = View.GONE
                    } else {
                        binding.arrowLeft.visibility = View.VISIBLE

                    }
                    if (position == NUM_PAGES - 1) {
                        binding.arrowRight.visibility = View.GONE
                    } else {
                        binding.arrowRight.visibility = View.VISIBLE

                    }
                }
            })
        }

        binding.arrowLeft.setOnClickListener {
            val cur = pager.currentItem
            pager.currentItem = cur - 1
        }

        binding.arrowRight.setOnClickListener {
            val cur = pager.currentItem
            pager.currentItem = cur + 1
        }

        binding.map.apply {
            val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this@GameViewFragment)
        }

        binding.footer.menuButton.apply {
            setOnClickListener {
                if (isMyVideo) {
                    turnOffMyVideo.invoke()
                }
                if (isMenu) {
                    closeMenu.invoke()
                } else {
                    openMenu.invoke()
                }
            }

        }
        binding.footer.faceButton.apply {
            setOnClickListener {
                if (isMenu) {
                    closeMenu.invoke()
                }
                if (isMyVideo) {
                    turnOffMyVideo.invoke()
                } else {
                    turnOnMyVideo.invoke()
                }
            }

        }

        return binding.root
    }

    private val turnOnMyVideo = {
        binding.peerContainer.visibility = View.VISIBLE
        binding.footer.faceButton.setImageResource(R.drawable.close_button)
        isMyVideo = !isMyVideo
    }

    private val turnOffMyVideo = {
        binding.peerContainer.visibility = View.GONE
        binding.footer.faceButton.setImageResource(R.drawable.face_button)
        isMyVideo = !isMyVideo
    }

    private val closeMenu = {
        binding.menu.root.visibility = View.GONE
        binding.footer.menuButton.setImageResource(R.drawable.menu)
        isMenu = !isMenu
    }

    private val openMenu = {
        binding.menu.root.visibility = View.VISIBLE
        binding.footer.menuButton.setImageResource(R.drawable.close_button)
        isMenu = !isMenu
    }

    private fun buttonPressed(view: View?) {
        if (binding.startFinishCall.text == resources.getString(R.string.hang_up)) {
            // Already connected to a session
            Log.d(TAG, "buttonPressed: leave")
            leaveSession()
            return
        }
//        if (arePermissionGranted()) {
        Log.d(TAG, "buttonPressed: join")
        initViews()
        viewToConnectingState()
        APPLICATION_SERVER_URL = getString(R.string.application_server_url)
        httpClient = CustomHttpClient(APPLICATION_SERVER_URL)
        val sessionId = binding.sessionName.text.toString()
        getToken(sessionId)
//        } else {
//            val permissionsFragment: DialogFragment = PermissionsDialogFragment()
//            permissionsFragment.show(getSupportFragmentManager(), "Permissions Fragment")
//        }
    }

    private fun setDefaultLocation() {

        //초기 위치를 서울로
        val DEFAULT_LOCATION = LatLng(37.56, 126.97)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, DEFAULT_ZOOM)
        mMap!!.moveCamera(cameraUpdate)
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        setDefaultLocation()
        mMap!!.apply {
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
        if (PermissionHelper.hasLocationPermission(requireContext())) { // 1. 위치 퍼미션을 가지고 있는지 확인
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식)
            startLocationUpdates() // 3. 위치 업데이트 시작
        } else {  //2. 권한이 없다면
            permissionLauncher.launch(
                arrayOf(
                    PermissionHelper.ACCESS_FINE_LOCATION,
                    PermissionHelper.ACCESS_COARSE_LOCATION
                )
            )

        }
    }

    fun getCurrentAddress(latlng: LatLng): String {
        //지오코더: GPS를 주소로 변환
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>?
        try {
            addresses = geocoder.getFromLocation(
                latlng.latitude,
                latlng.longitude,
                1
            )
        } catch (ioException: IOException) {
            //네트워크 문제
            Toast.makeText(context, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
            return "지오코더 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(context, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"
        }

        return if (addresses == null || addresses.isEmpty()) {
            Toast.makeText(context, "주소 발견 불가", Toast.LENGTH_LONG).show()
            "주소 발견 불가"
        } else {
            val address = addresses[0]
            address.getAddressLine(0).toString()
        }
    }

    fun setCurrentLocation(location: Location) {

        val currentLatLng = LatLng(location.latitude, location.longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng)
        mMap!!.moveCamera(cameraUpdate)
    }


    private fun checkLocationServicesStatus(): Boolean {
        val locationManager =
            activity?.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

    private fun getToken(sessionId: String) {
        try {
            // Session Request
            val sessionBody = "{\"customSessionId\": \"$sessionId\"}"
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            httpClient?.httpCall(
                "/api/sessions",
                "POST",
                "application/json",
                sessionBody,
                object : Callback {
                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        Log.d(TAG, "responseString: " + response.body.string())

                        // Token Request
                        val tokenBody =
                            "{}".toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                        httpClient!!.httpCall(
                            "/api/sessions/$sessionId/connections",
                            "POST",
                            "application/json",
                            tokenBody,
                            object : Callback {
                                override fun onResponse(call: Call, response: Response) {
                                    var responseString: String? = null
                                    try {
                                        responseString = response.body.string()
                                    } catch (e: IOException) {
                                        Log.e(TAG, "Error getting body", e)
                                    }
                                    getTokenSuccess(responseString, sessionId)
                                }

                                override fun onFailure(call: Call, e: IOException) {
                                    Log.e(
                                        TAG,
                                        "Error POST /api/sessions/SESSION_ID/connections",
                                        e
                                    )
                                    connectionError(APPLICATION_SERVER_URL)
                                }
                            })
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        Log.e(TAG, "Error POST /api/sessions", e)
                        connectionError(APPLICATION_SERVER_URL)
                    }
                })
        } catch (e: IOException) {
            Log.e(TAG, "Error getting token", e)
            e.printStackTrace()
            connectionError(APPLICATION_SERVER_URL)
        }
    }

    private fun getTokenSuccess(token: String?, sessionId: String) {
        // Initialize our session
        session = Session(sessionId, token, binding.pager, this)

        // Initialize our local participant and start local camera
        val participantName = binding.participantName.text.toString()
        val localParticipant =
            LocalParticipant(participantName, session, context, binding.localGlSurfaceView)
        localParticipant.startCamera()

        activity?.runOnUiThread {
            binding.mainParticipant.text = binding.participantName.text.toString()
            binding.mainParticipant.setPadding(20, 3, 20, 3)

        }


        // Initialize and connect the websocket to OpenVidu Server
        startWebSocket()
    }

    private fun startWebSocket() {
        val webSocket = CustomWebSocket(session, this)
        webSocket.execute()
        session?.setWebSocket(webSocket)
    }

    private fun connectionError(url: String?) {
        val myRunnable = Runnable {
            val toast: Toast =
                Toast.makeText(context, "Error connecting to $url", Toast.LENGTH_LONG)
            toast.show()
            viewToDisconnectedState()
        }
        Handler(requireContext().mainLooper).post(myRunnable)
    }

    private fun initViews() {
        val rootEglBase = EglBase.create()
        binding.localGlSurfaceView.init(rootEglBase.eglBaseContext, null)
        binding.localGlSurfaceView.setMirror(true)
        binding.localGlSurfaceView.setEnableHardwareScaler(true)
        binding.localGlSurfaceView.setZOrderMediaOverlay(true)
    }

    fun viewToDisconnectedState() {
        activity?.runOnUiThread {
            binding.localGlSurfaceView.clearImage()
            binding.localGlSurfaceView.release()
            binding.startFinishCall.text = resources.getString(R.string.start_button)
            binding.startFinishCall.isEnabled = true
            binding.applicationServerUrl.isEnabled = true
            binding.applicationServerUrl.isFocusableInTouchMode = true
            binding.sessionName.isEnabled = true
            binding.sessionName.isFocusableInTouchMode = true
            binding.participantName.isEnabled = true
            binding.participantName.isFocusableInTouchMode = true
            binding.mainParticipant.text = null
            binding.mainParticipant.setPadding(0, 0, 0, 0)
        }

    }

    fun viewToConnectingState() {
        activity?.runOnUiThread {
            binding.startFinishCall.isEnabled = false
            binding.applicationServerUrl.isEnabled = false
            binding.applicationServerUrl.isEnabled = false
            binding.sessionName.isEnabled = false
            binding.sessionName.isEnabled = false
            binding.participantName.isEnabled = false
            binding.participantName.isEnabled = false
        }
    }

    fun viewToConnectedState() {
        activity?.runOnUiThread {
            binding.startFinishCall.text = resources.getString(R.string.hang_up)
            binding.startFinishCall.isEnabled = true
        }
    }

    fun createRemoteParticipantVideo(remoteParticipant: RemoteParticipant) {
        val mainHandler: Handler = Handler(requireContext().mainLooper)
        val myRunnable = Runnable {
            val rowView: View =
                this.layoutInflater.inflate(R.layout.peer_video, null)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            lp.setMargins(0, 0, 0, 20)
            rowView.layoutParams = lp
            val rowId = View.generateViewId()
            rowView.id = rowId
            binding.pager.addView(rowView)
            val videoView =
                (rowView as ViewGroup).getChildAt(0) as SurfaceViewRenderer
            remoteParticipant.videoView = videoView
            videoView.setMirror(false)
            val rootEglBase = EglBase.create()
            videoView.init(rootEglBase.eglBaseContext, null)
            videoView.setZOrderMediaOverlay(true)
            val textView = rowView.getChildAt(1)
            remoteParticipant.participantNameText = textView as TextView
            remoteParticipant.view = rowView
            remoteParticipant.participantNameText.text = remoteParticipant.participantName
            remoteParticipant.participantNameText.setPadding(20, 3, 20, 3)
        }
        mainHandler.post(myRunnable)
    }

    fun setRemoteMediaStream(stream: MediaStream, remoteParticipant: RemoteParticipant) {
        val videoTrack = stream.videoTracks[0]
        videoTrack.addSink(remoteParticipant.videoView)
        activity?.runOnUiThread {
            remoteParticipant.videoView.visibility = View.VISIBLE
        }
    }

    fun leaveSession() {
        session?.leaveSession()
        httpClient?.dispose()
        viewToDisconnectedState()
    }

    private fun arePermissionGranted(): Boolean {
        return PermissionHelper.hasCameraPermission(requireContext()) && PermissionHelper.hasAudioPermission(
            requireContext()
        )
    }

    override fun onDestroy() {
        leaveSession()
        super.onDestroy()
    }

    override fun onStop() {
        leaveSession()
        mFusedLocationClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        // 위치서비스 활성화 여부 check
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
            if (PermissionHelper.hasLocationPermission(requireContext())) {
                mFusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null
                )
                if (mMap != null) mMap!!.isMyLocationEnabled = true
            }
        }
    }

    private val locationServiceLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                //사용자가 GPS를 켰는지 검사함
                if (checkLocationServicesStatus()) {
                    needRequest = true
                    return@registerForActivityResult
                } else {
                    Toast.makeText(
                        context,
                        "위치 서비스가 꺼져 있어, 현재 위치를 확인할 수 없습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    //여기부터는 GPS 활성화를 위한 메소드들
    private fun showDialogForLocationServiceSetting() {
        MyCustomDialog(requireContext(), object : MyCustomDialogInterface {
            override fun onYesButtonClicked() {
                locationServiceLauncher.launch(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                )
            }

            override fun onNoButtonClicked() {
            }

        }).apply {
            message = """
            앱을 사용하기 위해서는 위치 서비스가 필요합니다.
            위치 설정을 해주세요.
            """.trimIndent()
            yesMsg = "설정"
            show()
        }
    }

    private fun showPermissionDialog(
        msg: String,
        positive: String,
        negative: String
    ) {
        MyCustomDialog(requireContext(), object : MyCustomDialogInterface {
            override fun onYesButtonClicked() {
                PermissionHelper.launchPermissionSettings(requireActivity())
            }

            override fun onNoButtonClicked() {}
        }).apply {
            message = msg
            yesMsg = positive
            noMsg = negative
            show()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()

        if (PermissionHelper.hasLocationPermission(requireContext())) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
            if (mMap != null && checkLocationServicesStatus()) mMap!!.isMyLocationEnabled = true
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                leaveSession()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    companion object {
        private const val TAG = "추노_GameViewFragment"
        private const val NUM_PAGES = 5
        private const val UPDATE_INTERVAL = 1000L // 1초
        private const val MIN_UPDATE_INTERVAL = 500L // 0.5초
        private const val MIN_UPDATE_DISTANCE = 10f // 10m
        private const val DEFAULT_ZOOM = 17f
    }

    private inner class GameViewFragmentAdapter(fragment: Fragment) :
        FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            return ShopFragment()
        }
    }

}