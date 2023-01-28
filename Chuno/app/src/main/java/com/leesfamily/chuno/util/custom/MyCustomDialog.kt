package com.leesfamily.chuno.util.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.leesfamily.chuno.R

class MyCustomDialog(
    context: Context,
    myCustomDialogInterface: MyCustomDialogInterface
) : Dialog(context), View.OnClickListener {

    private var myCustomDialogInterface: MyCustomDialogInterface? = null

    // 인터페이스 연결
    init {
        this.myCustomDialogInterface = myCustomDialogInterface
    }

    var message: String? = null

    var yesMsg: String? = null

    var noMsg: String? = null

    var imageSrc: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.custom_dialog)
        Log.d(TAG, "onCreate: called")
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        message?.let {
            findViewById<TextView>(R.id.message_view).apply {
                text = it
                visibility = View.VISIBLE
            }
        }

        yesMsg?.let {
            findViewById<Button>(R.id.yes_button).apply {
                text = it
                visibility = View.VISIBLE

            }
        }

        noMsg?.let {
            findViewById<Button>(R.id.no_button).apply {
                text = it
                visibility = View.VISIBLE
            }
        }

        imageSrc?.let {
            findViewById<ImageView>(R.id.confirm_image_view).apply {
                this.setImageResource(it)
                visibility = View.VISIBLE
            }
        }

        findViewById<Button>(R.id.no_button).setOnClickListener(this)
        findViewById<Button>(R.id.yes_button).setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            findViewById<Button>(R.id.yes_button) -> {
                Log.d(TAG, "onCreate: Yes 버튼 클릭")
                this.myCustomDialogInterface?.onYesButtonClicked()
                dismiss()
            }
            findViewById<Button>(R.id.no_button) -> {
                Log.d(TAG, "onCreate: no 버튼 클릭")
                this.myCustomDialogInterface?.onNoButtonClicked()
                dismiss()
            }
        }
    }

//    fun setMessage(@StringRes stringId: Int) {
//        message = context.resources.getString(stringId)
//        Log.d(TAG, "setMessage: $message")
//    }
//
//    fun setYesMsg(@StringRes stringId: Int) {
//        yesMsg = context.resources.getString(stringId)
//
//    }
//
//    fun setNoMsg(@StringRes stringId: Int) {
//        noMsg = context.resources.getString(stringId)
//    }
//
//    fun setImageSrc(@DrawableRes imageId: Int) {
//        imageSrc = imageId
//    }

    fun setVisibility(target: String, visibility: Int) {
        when (target) {
            "image" -> {
                findViewById<Button>(R.id.confirm_image_view).visibility = visibility
            }
            "yes" -> {
                findViewById<Button>(R.id.yes_button).visibility = visibility
            }
            "no" -> {
                findViewById<Button>(R.id.no_button).visibility = visibility
            }
        }
    }

    companion object {
        const val TAG = "추노_CustomDialog"
    }
}