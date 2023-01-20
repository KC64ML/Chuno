package com.leesfamily.chuno

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.leesfamily.chuno.util.PreferenceUtil

class App : Application() {
    companion object{
        lateinit var context : App
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들
        context = this
        prefs = PreferenceUtil(applicationContext)

        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }
}