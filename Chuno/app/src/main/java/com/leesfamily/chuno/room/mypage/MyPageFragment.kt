package com.leesfamily.chuno.room.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.leesfamily.chuno.BuildConfig
import com.leesfamily.chuno.MainViewModel
import com.leesfamily.chuno.R
import com.leesfamily.chuno.util.login.LoginManager
import com.leesfamily.chuno.util.login.LoginPrefManager
import com.leesfamily.chuno.util.login.UserDB
import com.leesfamily.chuno.databinding.FragmentMyPageBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MyPageFragment : Fragment() {
    private lateinit var binding: FragmentMyPageBinding
    private var param1: String? = null
    private var param2: String? = null
    private val mainViewModel: MainViewModel by activityViewModels()
    private val myPageViewModel: MyPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(inflater, container, false)
        binding.toolbarInclude.toolbarTitle.text = getString(R.string.my_page_title)

        val user = mainViewModel.user.value
        if (user == null) {
            Log.d(TAG, "onCreateView: mainViewModel.user == null")
        }
        // https://i8d208.p.ssafy.io/api/resources/images?path=item/item2.png

        user?.let {
            binding.userName.text = it.nickname
            binding.userLevelText.text = it.level.toString()
            binding.userMoney.text = it.money.toString()
            binding.totalPlay.text = (it.chaserPlayCount + it.runnerPlayCount).toString()
            binding.chaserPlay.text = it.chaserPlayCount.toString()
            binding.runnerPlay.text = it.runnerPlayCount.toString()
            binding.winChaser.text = it.chaserWinCount.toString()
            binding.winRunner.text = it.runnerWinCount.toString()
            binding.losedPaper.text = it.paperCount.toString()

            it.profile.let {
                val server_url = BuildConfig.SERVER_URL
                val imageUrl =
                    "${server_url}/resources/images?path=${user.profile?.path}"
                Log.d(TAG, "onCreateView: imageUrl $imageUrl")

                Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.account)
                    .error(R.drawable.account)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .fallback(R.drawable.account)
                    .into(binding.profilePhoto)
            }
        }

        binding.logout.setOnClickListener {
            LoginManager.logout()
            LoginPrefManager.setLastLoginToken(null)
            mainViewModel.removeAllData()
            UserDB.setIsConfirmToken(false)
            Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            navigateLogin()
        }

        binding.unlink.setOnClickListener {
            Log.d(
                "추노",
                "onCreateView: LoginPrefManager.getLastLoginToken() ${LoginPrefManager.getLastLoginToken()}"
            )
            val token = mainViewModel.token.value
            Log.d("추노", "onCreateView: mainViewModel.token.value : ${token}")
            if (token == null) {
                navigateLogin()
            } else {
                myPageViewModel.deleteUser(mainViewModel.token.value!!)
            }

            myPageViewModel.isDeleted.observe(viewLifecycleOwner) {
                if (it) {
                    Log.d("추노", "onCreateView: isDeleted $it")
                    Toast.makeText(context, "회원이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    LoginManager.unlink()
                    LoginPrefManager.setLastLoginToken(null)
                    mainViewModel.removeAllData()
                    UserDB.setIsConfirmToken(false)
                    navigateLogin()
                } else {
                    Toast.makeText(context, "회원 삭제 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    private fun navigateLogin() {
        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.start_nav_host_fragment) as NavHostFragment
        navHostFragment.navController.navigate(R.id.loginFragment)
    }

    companion object {
        private const val TAG = "추노_MyPageFragment"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}