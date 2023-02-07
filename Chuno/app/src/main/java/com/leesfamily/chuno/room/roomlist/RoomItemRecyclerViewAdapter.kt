package com.leesfamily.chuno.room.roomlist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.FragmentRoomItemBinding
import com.leesfamily.chuno.network.data.Room
import com.leesfamily.chuno.util.custom.*


class RoomItemRecyclerViewAdapter(
    private val values: List<Room>,
    navigate: NavController, viewModel: RoomItemViewModel
) : RecyclerView.Adapter<RoomItemRecyclerViewAdapter.ViewHolder>(), CreateRoomDialogInterface,
    MyCustomDialogInterface {

    private lateinit var binding: FragmentRoomItemBinding
    private lateinit var manager: FragmentManager
    private lateinit var mContext: Context
    private lateinit var dialog1: CreateRoomDialog1
    private lateinit var dialog2: CreateRoomDialog2

    private var navigate: NavController? = null
    private var viewModel: RoomItemViewModel? = null

    init {
        this.navigate = navigate
        this.viewModel = viewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        binding = FragmentRoomItemBinding.inflate(
            LayoutInflater.from(mContext),
            parent,
            false
        )
        val holder = ViewHolder(binding)

        holder.itemView.setOnClickListener { view ->
            Log.d(TAG, "onCreateViewHolder: ${values[holder.layoutPosition]}")

            viewModel?.updateRoomData(values[holder.layoutPosition])
            navigate?.navigate(R.id.waitingRoomFragment)
        }
        binding.notifyButton.setOnClickListener {
            showCustomDialog(mContext)
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


        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val room = values[position]
        holder.roomNameView.text = room.title
        val dateTime = room.dateTime
        holder.reservationContent.text =
            "${dateTime.month}월 ${dateTime.day}일 ${dateTime.hour} : ${dateTime.minute}"
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentRoomItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val roomNameView: TextView = binding.roomName
        val reservationContent: TextView = binding.reservationContent
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


    companion object {
        private const val TAG = "추노_RoomItemRecyclerViewAda"
    }


}