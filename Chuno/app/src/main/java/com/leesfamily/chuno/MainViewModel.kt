package com.leesfamily.chuno

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.leesfamily.chuno.network.LoginGetter
import com.leesfamily.chuno.network.data.User
import com.leesfamily.chuno.start.PermissionFragment
import com.leesfamily.chuno.util.login.LoginPrefManager

/**
 * 핵심은 액티비티나 프래그먼트에서는 데이터를 획득해 UI에 보여주기만 하는 것이고
 * ViewModel안에서 그 데이터를 처리하고 가공하는 작업을 하는 것입니다.
 * */
class MainViewModel : ViewModel() {
    private val _user: MutableLiveData<User> by lazy {
        MutableLiveData<User>().also {
            loadUsers()
        }
    }

    var user: LiveData<User> = _user

    fun getUsers(userLise: User) {
        _user.value = userLise
    }

    // Model쪽에 User 값을 요청하는 메소드
    fun loadUsers() {

    }

    fun loadLoginInfo() {
        val lastToken = LoginPrefManager.getLastLoginToken() ?: return
        val mUser = LoginGetter().requestUser(lastToken)?.let {
//            user = it
        }

    }

//    fun showPermissionGuide(result: () -> Unit) {
//        Log.d("추노", "showPermissionGuide: $result")
//        PermissionFragment().setResultLambda {
//            result.invoke()
//        }
//    }

}
