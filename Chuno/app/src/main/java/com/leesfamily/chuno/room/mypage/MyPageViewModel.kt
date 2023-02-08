package com.leesfamily.chuno.room.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leesfamily.chuno.network.data.User
import com.leesfamily.chuno.network.login.LoginGetter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File

class MyPageViewModel : ViewModel() {
    private var _isDeleted: MutableLiveData<Boolean> = MutableLiveData()
    val isDeleted: LiveData<Boolean>
        get() = _isDeleted

//    private var _user: MutableLiveData<User> = MutableLiveData()
//    val user: LiveData<User>
//        get() = _user
//
//    fun setUser(user: User){
//        _user.value = user
//    }

    fun deleteUser(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { LoginGetter().requestDeleteUser(token) }
            _isDeleted.postValue(result.await())
        }

    }
}