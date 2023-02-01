package com.leesfamily.chuno.util.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.DateTimePickerBinding
import com.leesfamily.chuno.util.custom.DialogSizeHelper.dialogFragmentResize
import java.util.*

class DateTimePicker(
    context: Context,
    dateTimePickerInterface: DateTimePickerInterface
) : DialogFragment(), View.OnClickListener {
    private lateinit var binding: DateTimePickerBinding

    var mContext: Context? = null

    val curCal = Calendar.getInstance()

    // 예약일자
    var date: Calendar? = null

    // 예약시간
    var hourValue: Int? = null
    var minuteValue: Int? = null

    var isToday: Boolean = true

    private var dateTimePickerInterface: DateTimePickerInterface? = null

    init {
        this.dateTimePickerInterface = dateTimePickerInterface
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DateTimePickerBinding.inflate(inflater, container, false)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.todayButton.setOnClickListener(this)
        binding.tomorrowButton.setOnClickListener(this)
        binding.okButton.setOnClickListener(this)

        binding.timePicker.apply {
            Log.d(TAG, "onCreateView: ${curCal.time}")
            hour = curCal.get(Calendar.HOUR_OF_DAY)
            minute = curCal.get(Calendar.MINUTE)
            Log.d(TAG, "onCreateView: $hour:$minute")
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 0.8f)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.todayButton -> {
                if (!isToday) {
                    binding.todayButton.background =
                        ContextCompat.getDrawable(mContext!!, R.drawable.btn_pressed_left)
                    binding.tomorrowButton.background =
                        ContextCompat.getDrawable(mContext!!, R.drawable.btn_right_default_selector)
                    isToday = !isToday
                }
            }

            binding.tomorrowButton -> {
                if (isToday) {
                    binding.todayButton.background =
                        ContextCompat.getDrawable(mContext!!, R.drawable.btn_left_default_selector)
                    binding.tomorrowButton.background =
                        ContextCompat.getDrawable(mContext!!, R.drawable.btn_pressed_right)
                    isToday = !isToday
                }
            }

            binding.okButton -> {

                var isTrue = true

                if (isToday) {
                    date = curCal
                } else {
                    Calendar.getInstance().apply {
                        time = Date()
                        add(Calendar.DATE, 1)
                        date = this
                    }
                }

                binding.timePicker.apply {
                    if (isToday)
                        isTrue = checkTrueValue(curCal, hour, minute)
                    hourValue = hour
                    minuteValue = minute
                }

                Log.d(TAG, "onClick: $hourValue:$minuteValue")

                if (isTrue) {
                    dateTimePickerInterface?.onOkButtonClicked(
                        date,
                        hourValue,
                        minuteValue
                    )
                    dismiss()
                } else {
                    Toast.makeText(
                        mContext,
                        getString(R.string.date_warning_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun checkTrueValue(curCal: Calendar, resHour: Int, resMin: Int): Boolean {

        val curHour = curCal.get(Calendar.HOUR_OF_DAY)
        val curMin = curCal.get(Calendar.MINUTE)

        return if (curHour < resHour) {
            true
        } else if (curHour == resHour) {
            curMin <= resMin
        } else {
            false
        }
    }

    companion object {
        private const val TAG = "추노_DateTimePicker"
    }
}