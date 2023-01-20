package com.leesfamily.chuno.start

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
            pickImg()
        }
    }

    private fun init() {
        // activityResultLauncher 초기화
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == AppCompatActivity.RESULT_OK) {
                    val imageUri = it.data?.data ?: return@registerForActivityResult
                    setImgUri(imageUri)
                }
            }
//        photoResultLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//                if(it.resultCode == RESULT_OK){
//                    val extras = it.data?.extras
//                    val bitmap = extras?.get("data") as Bitmap?
//                    binding.ivImg.setImageBitmap(bitmap)
//                }
//                }
//            }
    }

    private fun takePicture() {
//        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        pictureIntent.resolveActivity(packageManager)?.also {
//            startActivityForResult(pictureIntent, REQ_IMG_CAPTURE)
//        }
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

    private fun pickImg() {
        Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            activityResultLauncher.launch(Intent.createChooser(this, "Get Album"))
        }
    }

    companion object {

    }
}