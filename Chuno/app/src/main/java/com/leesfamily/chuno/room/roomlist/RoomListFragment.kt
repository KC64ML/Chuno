package com.leesfamily.chuno.room.roomlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.leesfamily.chuno.databinding.FragmentRoomListBinding
import com.leesfamily.chuno.room.placeholder.PlaceholderContent

/**
 * 게임을 위한 방의 목록을 보여주는 Fragment이다.
 **/
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RoomListFragment : Fragment(){
    lateinit var binding: FragmentRoomListBinding
    private var param1: String? = null
    private var param2: String? = null
    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
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
}