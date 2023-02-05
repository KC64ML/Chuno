package com.leesfamily.chuno.room.rank

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.FragmentRankBinding
import com.leesfamily.chuno.openvidu.openvidu.LocalParticipant
import com.leesfamily.chuno.openvidu.openvidu.RemoteParticipant
import com.leesfamily.chuno.openvidu.openvidu.Session
import com.leesfamily.chuno.openvidu.utils.CustomHttpClient
import com.leesfamily.chuno.openvidu.websocket.CustomWebSocket
import com.leesfamily.chuno.util.PermissionHelper
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.webrtc.EglBase
import org.webrtc.MediaStream
import org.webrtc.SurfaceViewRenderer
import java.io.IOException
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class RankFragment : Fragment(), OnMapReadyCallback {
    private lateinit var callback: OnBackPressedCallback
    private lateinit var binding: FragmentRankBinding
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            0
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
//        getDeviceLocation(requireActivity(), requireContext(), fusedLocationProviderClient)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRankBinding.inflate(inflater, container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        val random = Random()
        val randomIndex = random.nextInt(100)
        binding.participantName.text = binding.participantName.text.append(randomIndex.toString())
        binding.startFinishCall.setOnClickListener {
            Log.d(TAG, "onCreateView: startFinishCall clicked")
            buttonPressed(it)
        }

        binding.map.apply {
            val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this@RankFragment)
        }
        return binding.root
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

    private var APPLICATION_SERVER_URL: String? = null
    private var session: Session? = null
    private var httpClient: CustomHttpClient? = null

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
                                    Log.e(TAG, "Error POST /api/sessions/SESSION_ID/connections", e)
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
//        session = Session(sessionId, token, binding.viewsContainer, this)

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
//        val webSocket = CustomWeb/*Socket(session, this)
//        webSocket.execute()
//        session?.setWebSocket(webSoc*/ket)
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
            binding.viewsContainer.addView(rowView)
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
        return PermissionHelper.hasCameraPermission(requireContext()) && PermissionHelper.hasAudioPermission(requireContext())
    }

    override fun onDestroy() {
        leaveSession()
        super.onDestroy()
    }

    override fun onStop() {
        leaveSession()
        super.onStop()
    }

    companion object {

        private const val TAG = "추노_RankFragment"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RankFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

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
    }
}