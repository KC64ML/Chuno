package com.leesfamily.chuno.room.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.FragmentSearchRoomBinding
import com.leesfamily.chuno.room.placeholder.PlaceholderContent


/**
 * 게임을 위한 방 목록 중 방제목으로(포함) 검색하는 Fragment이다.
 **/

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchRoomFragment : Fragment() {
    private var columnCount = 1
    lateinit var binding:FragmentSearchRoomBinding

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
        binding = FragmentSearchRoomBinding.inflate(inflater,container,false)
        binding.searchRoomList.apply {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = SearchItemRecyclerViewAdapter(PlaceholderContent.ITEMS)
        }
        return binding.root
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            SearchRoomFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}