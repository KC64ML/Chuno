package com.leesfamily.chuno.util.custom

import java.util.Calendar

interface DateTimePickerInterface {
    fun onOkButtonClicked(
        date: Calendar?,
        hourValue: Int?,
        minuteValue: Int?
    )
}