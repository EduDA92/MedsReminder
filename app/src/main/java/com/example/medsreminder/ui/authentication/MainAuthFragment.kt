package com.example.medsreminder.ui.authentication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.example.medsreminder.R
import com.example.medsreminder.databinding.FragmentMainAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainAuthFragment : Fragment() {

    private var _binding: FragmentMainAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainAuthBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        binding.logInButton.setOnClickListener {
            val action = MainAuthFragmentDirections.actionMainAuthFragmentToLogInFragment()
            navController.navigate(action)
        }
        binding.signUpButton.setOnClickListener {
            val action = MainAuthFragmentDirections.actionMainAuthFragmentToSingUpFragment()
            navController.navigate(action)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}