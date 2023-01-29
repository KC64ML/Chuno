package com.leesfamily.chuno.util.custom

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.CreateRoomDialog1Binding

class CreateRoomDialog1(
    context: Context,
    createRoomDialogInterface: CreateRoomDialogInterface
) : DialogFragment(), View.OnClickListener, MyCustomDialogInterface {
    private lateinit var binding: CreateRoomDialog1Binding

    private var createRoomDialogInterface: CreateRoomDialogInterface? = null

    var isReadOnly: Boolean = false

    var title: String? = null

    var password: String? = null

    var reservationDate: Long? = null

    var reservationTime: Long? = null

    // 인터페이스 연결
    init {
        this.createRoomDialogInterface = createRoomDialogInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        setReadOnly()
        isCancelable = false
        return binding.root
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.nextButton -> {
                if (checkEmptyInput(view))
                    createRoomDialogInterface?.onNextButtonClicked(binding.root)
            }
            binding.closeButton -> {
                dismiss()
            }
            binding.calendarImageView, binding.reservationDate, binding.reservationHour, binding.reservationMin -> {
                if (!isReadOnly)
                    createRoomDialogInterface?.onReservationClicked(binding.root)
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

    private fun showCustomDialog(flag: Int) {
        MyCustomDialog(requireContext(), this).apply {
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