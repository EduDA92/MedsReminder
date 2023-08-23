package com.example.medsreminder.ui.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.medsreminder.R
import com.example.medsreminder.databinding.FragmentUserBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class UserFragment : Fragment() {


    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* setting up the topAppBar */
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.dashboard, R.id.calendar, R.id.appointments, R.id.user
        ))
        binding.userTopAppBar.setupWithNavController(navController, appBarConfiguration)

        Log.e("MAIN", "Email" + viewModel.userData?.email.toString() + "User name" + viewModel.userData?.displayName.toString())

        binding.testLogoutButton.setOnClickListener {
            viewModel.signOut()
            val action = UserFragmentDirections.actionUserToAuth()
            navController.navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}