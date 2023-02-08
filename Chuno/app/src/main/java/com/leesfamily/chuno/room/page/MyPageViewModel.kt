package com.leesfamily.chuno.room.page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leesfamily.chuno.network.login.LoginGetter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MyPageViewModel : ViewModel() {
    private var _isDeleted: MutableLiveData<Boolean> = MutableLiveData()
    val isDeleted: LiveData<Boolean>
        get() = _isDeleted

    fun deleteUser(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { LoginGetter().requestDeleteUser(token) }
            _isDeleted.postValue(result.await())
        }

    }
}