package com.leesfamily.chuno.start

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leesfamily.chuno.network.data.RegisterUser
import com.leesfamily.chuno.network.data.User
import com.leesfamily.chuno.network.login.LoginGetter
import com.leesfamily.chuno.util.login.LoginPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class InputInfoViewModel : ViewModel() {
    private var _nickname: MutableLiveData<String> = MutableLiveData()
    val nickname: LiveData<String>
        get() = _nickname

    private var _email: MutableLiveData<String> = MutableLiveData()
    val email: LiveData<String>
        get() = _email

    private var _image: MutableLiveData<File> = MutableLiveData()
    val image: LiveData<File>
        get() = _image

    private var _phone: MutableLiveData<String> = MutableLiveData()
    val phone: LiveData<String>
        get() = _phone

    private var _registerSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val registerSuccess: LiveData<Boolean>
        get() = _registerSuccess

    private var _userInfo: MutableLiveData<User> = MutableLiveData()
    val userInfo: LiveData<User>
        get() = _userInfo


    fun setEmail(email: String) {
        _email.value = email
    }


    fun setPhone(phone: String) {
        _phone.value = phone
    }

    fun setNickName(nickname: String) {
        _nickname.value = nickname
    }

    fun setImage(bitmap: Bitmap, filePath: String) {
        bitmapConvertFile(bitmap, filePath)
    }

    fun register(navigate: () -> Unit) {
        Log.d("추노", "register: ${email.value}")
        if (email.value != null)
            viewModelScope.launch(Dispatchers.IO) {
                val token =
                    async {
                        LoginGetter().requestRegisterUser(
                            nickname.value!!,
                            email.value!!,
                            phone.value!!,
                            image.value
                        )
                    }
                LoginPrefManager.setLastLoginToken(token.await())
                Log.d("추노", "register: ${token.await()}")
                if (token.await() != null) {
                    _registerSuccess.postValue(true)
                } else {
                    _registerSuccess.postValue(false)
                }
                launch(Dispatchers.Main) {
                    navigate()
                }
            }
    }

    // 비트맵을 파일로 변환하는 메소드
    private fun bitmapConvertFile(bitmap: Bitmap, strFilePath: String) {
        // 파일 선언 -> 경로는 파라미터에서 받는다
        val file = File("${strFilePath}/profile.png")

        // OutputStream 선언 -> bitmap데이터를 OutputStream에 받아 File에 넣어주는 용도
        var out: OutputStream? = null
        try {
            out.use {
                // 파일 초기화
                file.createNewFile()

                // OutputStream에 출력될 Stream에 파일을 넣어준다
                out = FileOutputStream(file)

                // bitmap 압축
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)

                _image.value = file
                Log.d("추노", "bitmapConvertFile: ${_image.value}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}