package com.leesfamily.chuno.util.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.CreateRoomDialog1Binding
import com.leesfamily.chuno.util.custom.DialogSizeHelper.dialogFragmentResize
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class CreateRoomDialog1(
    context: Context,
    createRoomDialogInterface: CreateRoomDialogInterface
) : DialogFragment(), View.OnClickListener, MyCustomDialogInterface {
    private lateinit var binding: CreateRoomDialog1Binding

    private var createRoomDialogInterface: CreateRoomDialogInterface? = null

    var isReadOnly: Boolean = false

    // 방제목
    var title: String? = null

    // 비밀번호
    var password: String? = null

    // 예약일자
    var reservationDate: String? = null
    var yearValue: Int? = null
    var monthValue: Int? = null
    var dayValue: Int? = null

    // 예약시간
    var reservationHour: String? = null
    var reservationMin: String? = null

    var mContext: Context? = null

    // 최대 인원
    var step = 2
    var defValue = 6
    var minValue = 4
    var maxValue = 10
    var curValue = defValue

    // 인터페이스 연결
    init {
        this.createRoomDialogInterface = createRoomDialogInterface
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        mContext?.dialogFragmentResize(this, 0.8f)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CreateRoomDialog1Binding.inflate(inflater, container, false)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.nextButton.setOnClickListener(this)
        binding.closeButton.setOnClickListener(this)
        binding.calendarView.setOnClickListener(this)

        val numberView = binding.number.apply {
            text = defValue.toString()
        }
        binding.numberPlusBtn.apply {
            setOnClickListener {
                curValue += step
                checkRange(numberView, curValue, minValue, maxValue)
            }
        }

        binding.numberMinusBtn.apply {
            setOnClickListener {
                curValue -= step
                checkRange(numberView, curValue, minValue, maxValue)
            }
        }

        setReadOnly()
        isCancelable = false
        return binding.root
    }

    private fun checkRange(numberView: TextView, curValue: Int, minValue: Int, maxValue: Int) {

        when {
            curValue in minValue..maxValue -> {
                Log.d(TAG, "checkRange: minValue <= curValue <= maxValue")
                numberView.text = curValue.toString()
                binding.numberText.visibility = View.GONE
            }
            curValue < minValue -> {
                Log.d(TAG, "checkRange: minValue > curValue")
                numberView.text = minValue.toString()
                this.curValue = minValue
                binding.numberText.apply {
                    text = getString(R.string.min_person_count_message)
                    visibility = View.VISIBLE
                    setVisibilityGone(this)
                }
                binding.roomMaxCountEdit.animation =
                    AnimationUtils.loadAnimation(mContext, R.anim.shake)
            }
            else -> {
                Log.d(TAG, "checkRange: curValue > maxValue")
                numberView.text = maxValue.toString()
                this.curValue = maxValue
                binding.numberText.apply {
                    text = getString(R.string.max_person_count_message)
                    visibility = View.VISIBLE
                    setVisibilityGone(this)
                }
                binding.roomMaxCountEdit.animation =
                    AnimationUtils.loadAnimation(mContext, R.anim.shake)
            }
        }

    }

    private fun setVisibilityGone(textView: TextView) {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                binding.root.post {
                    textView.visibility = View.GONE
                }
                timer.cancel()
            }
        }, 1000)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.nextButton -> {
                if (checkEmptyInput(view)) {
                    createRoomDialogInterface?.onNextButtonClicked(binding.root)
                    dismiss()
                }
            }
            binding.closeButton -> {
                dismiss()
            }
            binding.calendarView -> {
                if (!isReadOnly) {
                    DateTimePicker(mContext!!, object : DateTimePickerInterface {

                        override fun onOkButtonClicked(
                            date: Calendar?,
                            hourValue: Int?,
                            minuteValue: Int?
                        ) {
                            val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")

                            reservationDate = date?.time?.let { df.format(it) }

                            yearValue = date?.get(Calendar.YEAR)
                            monthValue = date?.get(Calendar.DAY_OF_MONTH)
                            dayValue = date?.get(Calendar.DATE)

                            reservationHour = "$hourValue"
                            reservationMin = "$minuteValue"

                            binding.reservationDate.text = reservationDate
                            binding.reservationHour.text = reservationHour
                            binding.reservationMin.text = reservationMin
                        }

                    }).show(childFragmentManager, "datePicker")
                }
            }
        }
    }

    private fun setReadOnly() {
        if (isReadOnly) {
            Log.d(TAG, "setReadOnly: $isReadOnly")
            binding.titleView.text = getString(R.string.room_info_text)
            binding.roomTitleEdit.isEnabled = !isReadOnly
            binding.passwordEdit.isEnabled = !isReadOnly
        }
    }

    private fun checkEmptyInput(view: View?): Boolean {
        if (!isReadOnly) {
            view?.let {
                if (binding.roomTitleEdit.text.toString().isEmpty()) {
                    showCustomDialog(0)
                    return false
                } else {
                    this.title = binding.roomTitleEdit.text.toString()
                }
                if (binding.passwordEdit.text.toString().isEmpty()) {
                    showCustomDialog(1)
                    return false
                } else {
                    this.password = binding.passwordEdit.text.toString()
                }
            if (binding.reservationDate.text.toString().isEmpty() ||
                binding.reservationHour.text.toString().isEmpty() ||
                binding.reservationMin.text.toString().isEmpty()
            ) {
                showCustomDialog(2)
                return false
            } else {
//                this.reservationDate = binding.reservationDate.text
            }
                return true
            }
            return false
        }
        return true
    }

    private fun showCustomDialog(flag: Int) {
        MyCustomDialog(mContext!!, this).apply {
            when (flag) {
                0 -> {
                    message = getString(R.string.room_no_title_message)
                }
                1 -> {
                    message = getString(R.string.room_no_password_message)
                }
                2 -> {
                    message = getString(R.string.room_no_reservation_message)
                }
            }
            yesMsg = getString(R.string.ok)
            show()
        }
    }

    companion object {
        private const val TAG = "추노_CreateRoomDialog1"
    }

    override fun onYesButtonClicked() {
    }

    override fun onNoButtonClicked() {
    }
}