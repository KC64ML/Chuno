package com.leesfamily.chuno.util.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.fragment.app.DialogFragment
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.CreateRoomDialog2Binding

class CreateRoomDialog2(
    context: Context,
    createRoomDialogInterface: CreateRoomDialogInterface
) : DialogFragment(), MyCustomDialogInterface, View.OnClickListener {
    private lateinit var binding: CreateRoomDialog2Binding
    private var createRoomDialogInterface: CreateRoomDialogInterface? = null

    // 간격
    var step = 50

    // 최소
    var minValue = 300

    // 최대
    var maxValue = 1000

    // 기본값
    var defValue = 500

    // 방정보 보기 전용
    var isReadOnly: Boolean = false


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
        binding = CreateRoomDialog2Binding.inflate(inflater, container, false)

        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.yesButton.setOnClickListener(this)
        binding.closeButton.setOnClickListener(this)
        binding.noButton.setOnClickListener(this)
        val roundValue = binding.roomRoundValue.apply {
            text = defValue.toString()
        }

        binding.roomRoundEdit.apply {
            setSeekBarMax(this, maxValue)
            setSeekBarDefault(this, maxValue)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    setSeekBarChange(progress, roundValue)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        setReadOnly()
        isCancelable = false
        return binding.root
    }

    private fun setReadOnly() {
        if (isReadOnly) {
            Log.d(TAG, "setReadOnly: $isReadOnly")
            binding.titleView.text = getString(R.string.room_info_text)
            binding.roomRoundEdit.isEnabled = !isReadOnly
        }
    }

    private fun setSeekBarMax(sb: AppCompatSeekBar, max_value: Int) {
        sb.max = ((max_value - minValue) / step)
    }

    private fun setSeekBarDefault(sb: AppCompatSeekBar, max_value: Int) {
        sb.progress = sb.max / (max_value / defValue) - 1
    }

    private fun setSeekBarChange(progress: Int, tv: TextView) {
        val value = minValue + progress * step
        tv.text = value.toString()
    }

    private fun showCustomDialog() {
        MyCustomDialog(requireContext(), this).apply {
            message = "방 생성이 완료되었습니다."
            yesMsg = getString(R.string.ok)
            show()
        }
    }

    override fun onYesButtonClicked() {
    }

    override fun onNoButtonClicked() {
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.yesButton -> {
                Log.d(TAG, "onClick: 저장하여 서버와 통신")
                if (!isReadOnly)
                    showCustomDialog()
                dismiss()
            }
            binding.closeButton -> {
                dismiss()
            }
            binding.noButton -> {
                createRoomDialogInterface?.onPrevButtonClicked(binding.root)
                dismiss()
            }
        }
    }

    companion object {
        private const val TAG = "추노_CreateRoomDialog2"
    }

}
