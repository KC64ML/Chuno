package com.leesfamily.chuno.room.wait

import android.annotation.SuppressLint
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import androidx.annotation.MenuRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        binding = FragmentWaitingRoomListBinding.inflate(inflater, container, false)
        binding.toolbarInclude.toolbarTitle.text = "나는 방 이름"
        binding.toolbarInclude.toolbar.inflateMenu(R.menu.menu_toolbar)
        binding.userList.apply {

            layoutManager =
                GridLayoutManager(context, columnCount)

            adapter = UserItemRecyclerViewAdapter(PlaceholderContent.ITEMS)

        }

        binding.footer.readyButton.setOnClickListener {
                 findNavController().navigate(R.id.action_waitingRoomFragment_to_game_view)
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.room_info->{

                true
            }
            R.id.notify_off->{
                true
            }
            R.id.notify_on->{
                true
            }
            R.id.out->{
                true
            }
            else -> super.onContextItemSelected(item)
        }

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