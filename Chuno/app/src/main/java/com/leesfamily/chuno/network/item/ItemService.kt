package com.leesfamily.chuno.network.item

import com.leesfamily.chuno.network.data.ItemForm
import retrofit2.Call
import retrofit2.http.GET

interface ItemService {

    @GET("item")
    fun getItemData(): Call<ItemForm>
}