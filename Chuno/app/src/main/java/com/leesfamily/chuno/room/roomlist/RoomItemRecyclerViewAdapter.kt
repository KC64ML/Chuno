package com.leesfamily.chuno.room.roomlist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gun0912.tedpermission.provider.TedPermissionProvider.context
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.FragmentRoomItemBinding
import com.leesfamily.chuno.room.placeholder.PlaceholderContent.PlaceholderItem
import com.leesfamily.chuno.util.custom.*

class RoomItemRecyclerViewAdapter(
    private val values: List<PlaceholderItem>
) : RecyclerView.Adapter<RoomItemRecyclerViewAdapter.ViewHolder>(), CreateRoomDialogInterface,
    MyCustomDialogInterface {

    private lateinit var binding: FragmentRoomItemBinding
    private lateinit var manager: FragmentManager
    private lateinit var mContext: Context


    private lateinit var dialog1: CreateRoomDialog1
    private lateinit var dialog2: CreateRoomDialog2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        binding = FragmentRoomItemBinding.inflate(
            LayoutInflater.from(mContext),
            parent,
            false
        )
        binding.notifyButton.setOnClickListener {
            showCustomDialog(mContext)
        }
        binding.roomInView.setOnClickListener {
            Log.d(TAG, "onCreateViewHolder: 입장")

        }
        binding.roomInfoButton.setOnClickListener {
            manager = (mContext as AppCompatActivity).supportFragmentManager
            Log.d(TAG, "onCreateViewHolder: $manager")
            // 방 정보
            dialog1 = CreateRoomDialog1(mContext, this).apply {
                isReadOnly = true
                show(manager, "infoRoomDialog1")
            }
        }

        return ViewHolder(
            binding
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.roomNameView.text = item.id
        holder.reservationContent.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentRoomItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val roomNameView: TextView = binding.roomName
        val reservationContent: TextView = binding.reservationContent
//        val notifyButton: ImageButton = binding.notifyButton
//        val roomInButton: ImageButton = binding.roomInfoButton
    }

    private fun showCustomDialog(context: Context) {
        MyCustomDialog(context, this).apply {
            message = context.resources.getString(R.string.notify_on)
            yesMsg = context.resources.getString(R.string.ok)
            show()
        }
    }

    override fun onYesButtonClicked() {

    }

    override fun onNoButtonClicked() {

    }

    override fun onNextButtonClicked(view: View) {
        dialog2 = CreateRoomDialog2(mContext, this).apply {
            isReadOnly = true
            show(manager, "createRoomDialog2")
        }
    }

    override fun onPrevButtonClicked(view: View) {
        dialog1.show(manager, "createRoomDialog1")
    }

    override fun onReservationClicked(view: View) {
        Toast.makeText(context, "나 예약 버튼임", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "추노_RoomItemRecyclerViewAda"
    }
}