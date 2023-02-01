package com.leesfamily.chuno.room.wait

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.FragmentWaitingRoomListBinding
import com.leesfamily.chuno.room.wait.placeholder.PlaceholderContent

/**
 * A fragment representing a list of Items.
 */
class WaitingRoomFragment : Fragment() {
    private lateinit var binding: FragmentWaitingRoomListBinding
    private var columnCount = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.toolbarInclude.toolbarTitle.text = "나는 방 이름"
        binding = FragmentWaitingRoomListBinding.inflate(inflater, container, false)
        binding.userList.apply {

            layoutManager = when {
                columnCount <= 3 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = UserItemRecyclerViewAdapter(PlaceholderContent.ITEMS)

        }

        return binding.root
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            WaitingRoomFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}