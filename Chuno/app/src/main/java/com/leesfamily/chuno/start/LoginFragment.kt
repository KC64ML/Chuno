package com.leesfamily.chuno.start

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.leesfamily.chuno.MainViewModel
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.FragmentLoginBinding
import com.leesfamily.chuno.network.login.LoginGetter
import com.leesfamily.chuno.util.login.LoginListener
import com.leesfamily.chuno.util.login.LoginManager
import com.leesfamily.chuno.util.login.UserDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val myViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        if (UserDB.getIsConfirmToken()) {
            findNavController().navigate(R.id.homeFragment)
        }
        // lastToken이 null이면 회원가입을 해야하는 유저
        // null이 아니면서 유효하지않는 토큰이면 로그인 해야하는 유저

        binding.kakaoLogin.setOnClickListener {
            LoginManager.apply {
                setListener(object : LoginListener {
                    override fun loginSuccess(token: String) {
                        myViewModel.setToken(token)
                        if (myViewModel.email.value != null) {
                            findNavController().navigate(R.id.homeFragment)
                        } else {
                            findNavController().navigate(R.id.inputInfoFragment)
                        }
/*
                        lifecycleScope.launch(Dispatchers.IO) {
                            var result = LoginGetter().requestLogin(token)
                            Log.d(
                                TAG,
                                "loginSuccess: LoginGetter().requestLogin(token) : ${
                                    LoginGetter().requestLogin(token)
                                }"
                            )
                            launch(Dispatchers.Main) {
                                when (result) {
                                    "member" -> {
                                        Log.d(TAG, "loginSuccess: member")
                                        findNavController().navigate(R.id.homeFragment)
//                                        findNavController().navigate(R.id.inputInfoFragment)
                                    }
                                    "no_email" -> {
                                        Log.d(TAG, "loginSuccess: no_email")
                                        findNavController().navigate(R.id.inputInfoFragment)
                                    }
                                }
                            }
                        }*/
                    }

                    override fun loginFailed() {
                        Toast.makeText(context, "로그인을 해주세요", Toast.LENGTH_SHORT).show()
                    }
                })
                login(this@LoginFragment)
            }
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        private const val TAG = "추노_LoginFragment"
    }
}