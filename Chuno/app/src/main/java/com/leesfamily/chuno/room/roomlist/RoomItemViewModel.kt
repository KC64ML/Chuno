package com.leesfamily.chuno.room.roomlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.leesfamily.chuno.network.data.Player
import com.leesfamily.chuno.network.data.Room
import com.leesfamily.chuno.network.data.User

class RoomItemViewModel : ViewModel() {
    // 변경가능한 Mutable 타입의 LiveData
    private var _roomData: MutableLiveData<Room> = MutableLiveData()
    private var _players: ListLiveData<Player> = ListLiveData()

    // 무결성을 위한 Getter
    val roomData: LiveData<Room>
        get() = _roomData

    val players: ListLiveData<Player>
        get() = _players

    // setter
    fun updateRoomData(newRoomData: Room) {
        _roomData.value = newRoomData
    }

    fun setPlayers(user: ArrayList<Player>) {
        _players.value = user
        Log.d("추노", "user: ${user}")
        Log.d("추노", "addPlayer: ${_players.value}")
    }

    fun addPlayer(user: Player) {
        _players.value?.add(user)
        Log.d("추노", "user: ${user}")
        Log.d("추노", "addPlayer: ${_players.value}")
    }

    fun clearPlayer(){
        _players.clear(true)
    }

    fun getPlayer(index: Int) {
        _players.value?.get(index)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("추노", "onCleared: RoomItemViewModel")
    }
}

class ListLiveData<T> : MutableLiveData<ArrayList<T>?>() {
    init {
        value = ArrayList()
    }

    fun add(item: T) {
        val items: ArrayList<T>? = value
        items!!.add(item)
        value = items
    }

    fun addAll(list: List<T>?) {
        val items: ArrayList<T>? = value
        items!!.addAll(list!!)
        value = items
    }

    fun clear(notify: Boolean) {
        val items: ArrayList<T>? = value
        items!!.clear()
        if (notify) {
            value = items
        }
    }

    fun remove(item: T) {
        val items: ArrayList<T>? = value
        items!!.remove(item)
        value = items
    }

    fun notifyChange() {
        val items: ArrayList<T>? = value
        value = items
    }
}