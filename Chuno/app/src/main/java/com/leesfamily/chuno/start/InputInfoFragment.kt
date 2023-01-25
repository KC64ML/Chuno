package com.leesfamily.chuno.start

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.FragmentInputInfoBinding


class InputInfoFragment : Fragment() {
    private lateinit var binding: FragmentInputInfoBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var photoResultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInputInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profile.setOnClickListener {
            startDialog()
        }
        binding.saveButton.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        binding.editNick.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // 입력란에 변화가 있을 시 조치
                Log.d(TAG, "onTextChanged: s:$s,start:$start,before:$before,count:$count")
            }

            override fun afterTextChanged(arg0: Editable) {
                // 입력이 끝났을 때 조치
                // 닉네임 중복 확인
                val count = arg0.length
                if (count in 1..6) {
                    ableButton(true)
                    binding.limitCount.visibility = View.GONE
                    Log.d(TAG, "onTextChanged: 1~6")
                } else if (count > 6) {
                    ableButton(false)
                    binding.limitCount.visibility = View.VISIBLE
                    Log.d(TAG, "onTextChanged: 6초과")
                } else {
                    ableButton(false)
                    Log.d(TAG, "onTextChanged: 0")
                }
                Log.d(TAG, "afterTextChanged: $arg0")

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // 입력하기 전에 조치
                // 최대 6자리인 것을 표시
                Log.d(TAG, "beforeTextChanged: s:$s,start:$start,count:$count,after:$after")
            }
        })
    }

    private fun ableButton(flag: Boolean) {
        binding.saveButton.apply {
            when (flag) {
                true -> {
                    isClickable = true
                    backgroundTintList =
                        ColorStateList.valueOf(requireActivity().getColor(R.color.primary))
                    Log.d(TAG, "ableButton: true")
                }
                false -> {
                    isClickable = false
                    backgroundTintList = ColorStateList.valueOf(Color.parseColor("#B9B7BD"))
                    Log.d(TAG, "ableButton: false")
                }
            }

        }
    }

    private fun init() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == AppCompatActivity.RESULT_OK) {
                    val imageUri = it.data?.data ?: return@registerForActivityResult
                    setImgUri(imageUri)
                }
            }
        photoResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == AppCompatActivity.RESULT_OK) {
                    val extras = it.data?.extras
                    val bitmap = extras?.get("data") as Bitmap?
                    binding.profile.setImageBitmap(bitmap)
                }
            }

    }

    private fun setImgUri(imgUri: Uri) {
        imgUri.let {
            val bitmap: Bitmap
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(
                    requireContext().contentResolver,
                    imgUri
                )
                binding.profile.setImageBitmap(bitmap)
            } else {
                val source =
                    ImageDecoder.createSource(requireContext().contentResolver, imgUri)
                bitmap = ImageDecoder.decodeBitmap(source)
                binding.profile.setImageBitmap(bitmap)
            }
        }
    }

    private fun startDialog() {
        val options = arrayOf<CharSequence>(
            "사진", "앨범"
        )
        val builder: AlertDialog.Builder = AlertDialog.Builder(
            requireContext()
        )

        builder.apply {
            setTitle("사진/앨범")

            setItems(
                options,
                DialogInterface.OnClickListener { dialog, which ->
                    if (options[which] == "사진") {
                        try {
                            val cameraIntent = Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE
                            )
                            photoResultLauncher.launch(cameraIntent)
                        } catch (ex: ActivityNotFoundException) {
                            val errorMessage =
                                "이미지를 캡쳐할 수 없습니다. 다시 시도해주세요."
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    } else if (options[which] == "앨범") {
                        Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "image/*"
                            activityResultLauncher.launch(Intent.createChooser(this, "Get Album"))
                        }
                    }
                })
            create()
            show()
        }
    }

    companion object {
        private const val TAG = "추노_InputInfo"

    }
}