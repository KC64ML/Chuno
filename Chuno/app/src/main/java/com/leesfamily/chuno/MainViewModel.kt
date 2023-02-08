package com.leesfamily.chuno

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.leesfamily.chuno.network.data.Item
import com.leesfamily.chuno.network.data.Room
import com.leesfamily.chuno.network.data.User
import com.leesfamily.chuno.network.item.ItemGetter
import com.leesfamily.chuno.network.login.LoginGetter
import com.leesfamily.chuno.start.LoginFragment
import com.leesfamily.chuno.util.login.LoginPrefManager
import com.leesfamily.chuno.util.login.UserDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * 핵심은 액티비티나 프래그먼트에서는 데이터를 획득해 UI에 보여주기만 하는 것이고
 * ViewModel안에서 그 데이터를 처리하고 가공하는 작업을 하는 것입니다.
 * */

class MainViewModel : ViewModel() {
    //    private var roomList: MutableLiveData<List<Room>> = MutableLiveData()
    private var _itemList: List<Item> = listOf()
    val itemList: List<Item>
        get() = _itemList

    private var _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User>
        get() = _user

    private var _token: MutableLiveData<String> = MutableLiveData()
    val token: LiveData<String>
        get() = _token

    private var location: MutableLiveData<LatLng> = MutableLiveData()

    private var _email: MutableLiveData<String> = MutableLiveData()
    val email: LiveData<String>
        get() = _email

    private var _isNew: MutableLiveData<Boolean> = MutableLiveData()
    val isNew: LiveData<Boolean>
        get() = _isNew


    fun setUserInfo() {
        LoginPrefManager.getLastLoginToken()?.let { token ->
            viewModelScope.launch(Dispatchers.IO) {
                LoginGetter().requestUser(token)?.let { user ->
                    this@MainViewModel._user.postValue(user)
                }
            }
        }
    }

    fun setToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            LoginGetter().requestLogin(token).run {
                Log.d("추노", "setToken: this.code ${this?.code}")
                when (this?.code) {
                    "member" -> {
                        LoginPrefManager.setLastLoginToken(this.result)
                        Log.d("추노", "setToken:${LoginPrefManager.getLastLoginToken()} ")
                        _token.postValue(this.result)
                        _isNew.postValue(false)
                    }
                    "no_email" -> {
                        _email.postValue(this.result)
                        Log.d("추노_MainViewModel", "setToken: _email : $_email")
                        _isNew.postValue(true)
                    }
                }
            }
        }
    }

    fun setLastToken(token: String) {
        Log.d("추노_MainViewModel", "setLastToken: ")
        _token.value = token
    }

    fun isLogin(): Boolean {
        return _token.value != null
    }

    fun removeAllData() {
        _user = MutableLiveData()
        _token = MutableLiveData()
        _email = MutableLiveData()
        Log.d("추노", "removeAllData:$_user")
        Log.d("추노", "removeAllData: $_token")
        Log.d("추노", "removeAllData: $_email")
    }

    fun setItemList() {
        viewModelScope.launch(Dispatchers.IO) {
            ItemGetter().requestItem()?.let {
                _itemList = it
                Log.d("추노", "setItemList: _itemList ${_itemList}")
            }
        }
    }

    // location
    fun getLocation(): MutableLiveData<LatLng> = location

    fun setLocation(location: LatLng) {
        this.location.value = location
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("추노 MainViewModel", "onCleared: ")
    }
}
