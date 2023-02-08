package com.leesfamily.chuno.room.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.leesfamily.chuno.network.data.Item

class ShopViewModel : ViewModel() {
    // 변경가능한 Mutable 타입의 LiveData
    private var _itemList: MutableLiveData<List<Item>> = MutableLiveData()

    // 무결성을 위한 Getter
    val itemList: LiveData<List<Item>>
        get() = _itemList

    fun setItemList(itemList: List<Item>) {
        _itemList.value = itemList
    }


}