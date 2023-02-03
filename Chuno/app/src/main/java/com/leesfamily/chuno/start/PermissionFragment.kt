package com.leesfamily.chuno.start

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.leesfamily.chuno.MainActivity
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.FragmentPermissionBinding
import com.leesfamily.chuno.util.PermissionHelper
import com.leesfamily.chuno.util.custom.MyCustomDialog
import com.leesfamily.chuno.util.custom.MyCustomDialogInterface


class PermissionFragment : Fragment() {
    private lateinit var binding: FragmentPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPermissionBinding.inflate(layoutInflater)
        binding.okButton.setOnClickListener {
            permissionLauncher.launch(
                arrayOf(
                    PermissionHelper.CAMERA_PERMISSION,
//                    PermissionHelper.CALL_PHONE_PERMISSION,
                    PermissionHelper.RECORD_AUDIO,
                    PermissionHelper.ACCESS_FINE_LOCATION,
                    PermissionHelper.ACCESS_COARSE_LOCATION
                )
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "onDestroy: f")
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value

                if (!isGranted) {
                    if (permissionName == PermissionHelper.CAMERA_PERMISSION)
                        showPermissionDialog(
                            getString(R.string.camera_permission_message),
                            getString(R.string.setting),
                            getString(R.string.cancel)
                        )
                    else if (permissionName == PermissionHelper.RECORD_AUDIO)
                        showPermissionDialog(
                            getString(R.string.record_permission_message),
                            getString(R.string.setting),
                            getString(R.string.cancel)
                        )
                   /* else if (permissionName == PermissionHelper.CALL_PHONE_PERMISSION)
                        showPermissionDialog(
                            getString(R.string.call_permission_message),
                            getString(R.string.setting),
                            getString(R.string.cancel)
                        )*/
                    else if (permissionName == PermissionHelper.ACCESS_COARSE_LOCATION || permissionName == PermissionHelper.ACCESS_FINE_LOCATION)
                        showPermissionDialog(
                            getString(R.string.location_permission_message),
                            getString(R.string.setting),
                            getString(R.string.cancel)
                        )
                }
                if (PermissionHelper.hasAudioPermission(requireContext()) && PermissionHelper.hasCameraPermission(
                        requireContext()
                    ) &&
                    /*PermissionHelper.hasCallPhonePermission(requireContext()) &&*/ PermissionHelper.hasLocationPermission(
                        requireContext()
                    )
                ) {
                    findNavController().navigate(R.id.loginFragment)
                }
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
}