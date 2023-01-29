package com.leesfamily.chuno.room.roomlist

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.FragmentRoomListBinding
import com.leesfamily.chuno.room.placeholder.PlaceholderContent
import com.leesfamily.chuno.util.custom.CreateRoomDialog1
import com.leesfamily.chuno.util.custom.CreateRoomDialogInterface

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
    private lateinit var viewModel:RoomItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[RoomItemViewModel::class.java]
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoomListBinding.inflate(inflater, container, false)

        binding.myRoomList.apply {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(requireContext())
                else -> GridLayoutManager(requireContext(), columnCount)
            }
            adapter = RoomItemRecyclerViewAdapter(PlaceholderContent.ITEMS)
        }
        binding.allRoomList.apply {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(requireContext())
                else -> GridLayoutManager(requireContext(), columnCount)
            }
            adapter = RoomItemRecyclerViewAdapter(PlaceholderContent.ITEMS)

        }

        val fab: View = binding.createRoom
        fab.setOnClickListener { view ->
            val childFragmentManager = childFragmentManager
            childFragmentManager.findFragmentByTag("createRoomDialog1")?.let {
                childFragmentManager.beginTransaction().remove(it)
            }
            CreateRoomDialog1(requireContext(), this).apply {
                show(childFragmentManager, "createRoomDialog1")
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RoomListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putInt(RoomItemFragment.ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    override fun onNextButtonClicked(view: View) {
        Toast.makeText(context, "나 다음 버튼임", Toast.LENGTH_SHORT).show()
        DatePickerDialog(requireContext())
    }

    override fun onReservationClicked(view: View) {
        Toast.makeText(context, "나 예약 버튼임", Toast.LENGTH_SHORT).show()

    }
}