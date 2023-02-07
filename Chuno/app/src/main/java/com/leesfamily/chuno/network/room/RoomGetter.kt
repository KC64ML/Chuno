package com.leesfamily.chuno.network.room

import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.leesfamily.chuno.network.ChunoServer
import com.leesfamily.chuno.network.data.Room
import com.leesfamily.chuno.network.data.RoomForm
import retrofit2.Response

class RoomGetter {
    private var roomData: RoomForm? = null

    fun requestRoomList(location: LatLng): List<Room>? {

        val itemResponse: Response<RoomForm> =
            ChunoServer.roomServer.getRoomList(location.latitude, location.longitude).execute()

        val networkResponse = itemResponse.raw().networkResponse?.code
        val requestCode = itemResponse.code()
        if (requestCode == 200) {
            roomData = itemResponse.body()
            val roomList: List<Room>? = roomData?.result

            Log.d("추노_item", "requestLogin: request success, $roomList")
            return roomList
        }
        return null
    }

}