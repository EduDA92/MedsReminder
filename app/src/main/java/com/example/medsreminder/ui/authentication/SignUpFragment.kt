package com.example.medsreminder.ui.authentication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.core.widget.addTextChangedListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.medsreminder.R
import com.example.medsreminder.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SingUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthenticationViewModel by hiltNavGraphViewModels(R.id.auth)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* setting up the topAppBar */
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.signUpTopAppBar.setupWithNavController(navController, appBarConfiguration)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signUpResponse.collect { uiState ->
                    when (uiState) {
                        is AuthUiState.LoginError -> {
                            Snackbar.make(binding.root, uiState.msg, Snackbar.LENGTH_SHORT)
                                .show()
                        }

                        is AuthUiState.LoginSuccess -> {
                            /* Check if user signUp successfully */
                            if (uiState.success) {
                                val action = SingUpFragmentDirections.actionGlobalDashboard()
                                navController.navigate(action)
                            }
                        }

                    }

                }
            }
        }

        binding.createAccountButton.setOnClickListener {
            viewModel.createUser(
                binding.emailTextFieldLayout.editText?.text.toString(),
                binding.passwordTextFieldLayout.editText?.text.toString()
            )
        }


        /* Enable/disable SingUp Button */

        binding.emailTextFieldEditText.addTextChangedListener {
            binding.createAccountButton.isEnabled =
                        binding.emailTextFieldLayout.editText?.text?.isNotEmpty() == true &&
                        binding.passwordTextFieldLayout.editText?.text?.isNotEmpty() == true
        }

        binding.passwordTextFieldEditText.addTextChangedListener {
            binding.createAccountButton.isEnabled =
                        binding.emailTextFieldLayout.editText?.text?.isNotEmpty() == true &&
                        binding.passwordTextFieldLayout.editText?.text?.isNotEmpty() == true
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}