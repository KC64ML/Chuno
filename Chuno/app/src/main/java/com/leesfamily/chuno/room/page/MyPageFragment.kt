package com.leesfamily.chuno.room.page

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.leesfamily.chuno.MainViewModel
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.FragmentMyPageBinding
import com.leesfamily.chuno.network.login.LoginGetter
import com.leesfamily.chuno.util.login.LoginManager
import com.leesfamily.chuno.util.login.LoginPrefManager
import com.leesfamily.chuno.util.login.UserDB

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

        binding.logout.setOnClickListener {
            LoginManager.logout(this)
            LoginPrefManager.setLastLoginToken(null)
            mainViewModel.removeAllData()
            UserDB.setIsConfirmToken(false)
            Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            navigate()
        }

        binding.unlink.setOnClickListener {
            Log.d(
                "추노",
                "onCreateView: LoginPrefManager.getLastLoginToken() ${LoginPrefManager.getLastLoginToken()}"
            )
            val token = mainViewModel.token.value
            Log.d("추노", "onCreateView: mainViewModel.token.value : ${token}")
            if (token == null) {
                navigate()
            } else {
                myPageViewModel.deleteUser(mainViewModel.token.value!!)
            }

            myPageViewModel.isDeleted.observe(viewLifecycleOwner) {
                if (it) {
                    Log.d("추노", "onCreateView: isDeleted $it")
                    Toast.makeText(context, "회원이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    LoginManager.unlink(this)
                    LoginPrefManager.setLastLoginToken(null)
                    mainViewModel.removeAllData()
                    UserDB.setIsConfirmToken(false)
                    navigate()
                } else {
                    Toast.makeText(context, "회원 삭제 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    private fun navigate() {
        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.start_nav_host_fragment) as NavHostFragment
        navHostFragment.navController.navigate(R.id.loginFragment)
    }

    companion object {

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