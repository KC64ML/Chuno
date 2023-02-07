package com.leesfamily.chuno.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class GameViewModel : ViewModel() {
    private var inputText: MutableLiveData<CharSequence> = MutableLiveData()

    private var _currentPosition: MutableLiveData<LatLng> = MutableLiveData()

    val currentPosition: LiveData<LatLng>
        get() = _currentPosition




    fun getData(): MutableLiveData<CharSequence> = inputText

    fun updateText(input: CharSequence) {
        inputText.value = input
    }
}