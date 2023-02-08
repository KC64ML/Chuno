package com.leesfamily.chuno.game.wait

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.leesfamily.chuno.databinding.WaitingRoomChatItemBinding
import com.leesfamily.chuno.network.data.Chat

class ChatItemRecyclerViewAdapter(
    private val values: ArrayList<Chat>?,
) : RecyclerView.Adapter<ChatItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            WaitingRoomChatItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (values != null) {
            val item = values[position]
            holder.idView.text = item.nickName
            holder.contentView.text = item.content
        }
    }

    override fun getItemCount(): Int = values?.size ?: 0

    inner class ViewHolder(binding: WaitingRoomChatItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.chatName
        val contentView: TextView = binding.chatContent

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}