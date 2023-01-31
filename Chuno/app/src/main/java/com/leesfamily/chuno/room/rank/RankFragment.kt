package com.leesfamily.chuno.room.rank

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.fragment.app.Fragment
import com.leesfamily.chuno.databinding.FragmentRankBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class RankFragment : Fragment() {

    private lateinit var binding: FragmentRankBinding
    private var param1: String? = null
    private var param2: String? = null


    val maxValue = 1000
    val minValue = 300
    val step = 50
    val defaultValue = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            0
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRankBinding.inflate(inflater, container, false)

        val roundValue = binding.roomRoundValue

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

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            })
//            setSeekBarAnimation(this)
        }
        return binding.root
    }

    private fun setSeekBarMax(sb: AppCompatSeekBar, max_value: Int) {
        sb.max = ((max_value - minValue) / step)
    }

    private fun setSeekBarDefault(sb: AppCompatSeekBar, max_value: Int) {
        sb.progress = sb.max / (max_value / defaultValue) - 1
    }

    private fun setSeekBarChange(progress: Int, tv: TextView) {
        val value = minValue + progress * step
        tv.text = value.toString()
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RankFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}