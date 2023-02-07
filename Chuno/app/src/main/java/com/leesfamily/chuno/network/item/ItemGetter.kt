package com.leesfamily.chuno.network.item

import android.util.Log
import com.leesfamily.chuno.network.ChunoServer
import com.leesfamily.chuno.network.data.Item
import com.leesfamily.chuno.network.data.ItemForm
import retrofit2.Response

class ItemGetter {
    private var itemData: ItemForm? = null

    fun requestItem(): List<Item>? {

        val itemResponse: Response<ItemForm> = ChunoServer.itemServer.getItemData().execute()

        val networkResponse = itemResponse.raw().networkResponse?.code
        val requestCode = itemResponse.code()
        if (requestCode == 200) {
            itemData = itemResponse.body()
            val item: List<Item>? = itemData?.result

            Log.d("추노_item", "requestLogin: request success, $item")
            return item
        }
        return null
    }


}