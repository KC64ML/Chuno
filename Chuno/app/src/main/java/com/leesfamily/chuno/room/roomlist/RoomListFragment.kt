package com.leesfamily.chuno.room.roomlist

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.FragmentRoomListBinding
import com.leesfamily.chuno.network.room.RoomGetter
import com.leesfamily.chuno.util.PermissionHelper
import com.leesfamily.chuno.util.custom.CreateRoomDialog1
import com.leesfamily.chuno.util.custom.CreateRoomDialog2
import com.leesfamily.chuno.util.custom.CreateRoomDialogInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 게임을 위한 방의 목록을 보여주는 Fragment이다.
 **/
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RoomListFragment : Fragment(), CreateRoomDialogInterface {
    lateinit var binding: FragmentRoomListBinding
    private var param1: String? = null
    private var param2: String? = null
    private var columnCount = 1

    private val viewModel:RoomItemViewModel by activityViewModels()

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private lateinit var dialog1: CreateRoomDialog1
    private lateinit var dialog2: CreateRoomDialog2
    lateinit var updateRoomList: Task<Location>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoomListBinding.inflate(inflater, container, false)
        binding.toolbarInclude.toolbarTitle.text = getString(R.string.room_list_title)
        if (PermissionHelper.hasLocationPermission(requireContext())) {
            getRoomList()
            binding.refreshLayout.apply {
                setOnRefreshListener {
                    // 새로고침 코드를 작성
                    Log.d(TAG, "onCreateView: refreshing recyclerview")
                    if (updateRoomList.isComplete)
                        getRoomList()
                    // 새로고침 완료시, 새로고침 아이콘이 사라질 수 있게 isRefreshing = false
                    this.isRefreshing = false

                }
            }

            val fab: View = binding.createRoom
            fab.setOnClickListener { view ->
                val childFragmentManager = childFragmentManager
                childFragmentManager.findFragmentByTag("createRoomDialog1")
                    ?.let {
                        childFragmentManager.beginTransaction().remove(it)
                    }
                dialog1 = CreateRoomDialog1(requireContext(), this).apply {
                    show(childFragmentManager, "createRoomDialog1")
                }

            }
        }
        return binding.root
    }

    @SuppressLint("MissingPermission")
    fun getRoomList() {
        updateRoomList =
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    Log.d(TAG, "onCreateView: roomList 받아오기")
                    lifecycleScope.launch(Dispatchers.IO) {
                        RoomGetter().requestRoomList(
                            LatLng(
                                location.latitude,
                                location.longitude
                            )
                        )
                            ?.let { roomList ->

                                launch(Dispatchers.Main) {
                                    binding.myRoomList.apply {
                                        layoutManager = when {
                                            columnCount <= 1 -> LinearLayoutManager(
                                                requireContext()
                                            )
                                            else -> GridLayoutManager(
                                                requireContext(),
                                                columnCount
                                            )
                                        }
                                        adapter =
                                            RoomItemRecyclerViewAdapter(
                                                roomList,
                                                navigate(),
                                                viewModel
                                            )
                                    }
                                    binding.allRoomList.apply {
                                        layoutManager = when {
                                            columnCount <= 1 -> LinearLayoutManager(
                                                requireContext()
                                            )
                                            else -> GridLayoutManager(
                                                requireContext(),
                                                columnCount
                                            )
                                        }
                                        adapter =
                                            RoomItemRecyclerViewAdapter(
                                                roomList,
                                                navigate(),
                                                viewModel
                                            )

                                    }
                                    binding.loadingBar.root.visibility = View.GONE
                                    binding.refreshLayout.visibility = View.VISIBLE
                                    binding.createRoom.visibility = View.VISIBLE
                                }

                            }
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun navigate(): NavController {
        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.start_nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }

    override fun onNextButtonClicked(view: View) {
        val childFragmentManager = childFragmentManager
        childFragmentManager.findFragmentByTag("createRoomDialog1")?.let {
            childFragmentManager.beginTransaction().remove(it)
        }
        dialog2 = CreateRoomDialog2(requireContext(), this).apply {
            show(childFragmentManager, "createRoomDialog2")
        }
    }

    override fun onPrevButtonClicked(view: View) {
        dialog1.show(childFragmentManager, "createRoomDialog1")
    }

    companion object {
        private const val TAG = "추노_RoomListFragment"
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RoomListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }


}