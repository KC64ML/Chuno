package com.leesfamily.chuno.room

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.leesfamily.chuno.MainActivity
import com.leesfamily.chuno.MainViewModel
import com.leesfamily.chuno.R
import com.leesfamily.chuno.databinding.HomeFragmentBinding
import com.leesfamily.chuno.util.login.LoginPrefManager
import com.leesfamily.chuno.util.login.UserDB

class HomeFragment : Fragment() {
    private lateinit var binding: HomeFragmentBinding
    lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)

//        val navHostFragment =
//            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val graphInflater = navHostFragment.navController.navInflater
//        val navGraph = graphInflater.inflate(R.navigation.nav_graph)
//
//        navController.graph = navGraph

//        val startDestination =R.id.room_list
//        navGraph.setStartDestination(startDestination)
        navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)

        childFragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->
            Log.d(TAG, "addFragmentOnAttachListener: ")
            if (UserDB.getIsConfirmToken())
                LoginPrefManager.getLastLoginToken()?.let {
                    mainViewModel.setLastToken(it)
                }
            else {
                navigate()
            }
        }

        return binding.root
    }

    fun navigate() {
        navHostFragment.findNavController().navigate(R.id.loginFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        private const val TAG = "추노_HomeFragment"
    }

}