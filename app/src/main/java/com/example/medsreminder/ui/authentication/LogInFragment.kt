package com.example.medsreminder.ui.authentication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.medsreminder.R
import com.example.medsreminder.databinding.FragmentLogInBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthenticationViewModel by hiltNavGraphViewModels(R.id.auth)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLogInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* setting up the topAppBar */
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.logInTopAppBar.setupWithNavController(navController, appBarConfiguration)


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signInResponse.collect { uiState ->
                    when (uiState) {
                        is AuthUiState.LoginError -> {
                            Snackbar.make(binding.root, uiState.msg, Snackbar.LENGTH_SHORT)
                                .show()
                        }

                        is AuthUiState.LoginSuccess -> {
                            /* Check if user successfully Logged In */
                            if (uiState.success) {
                                val action = SingUpFragmentDirections.actionGlobalDashboard()
                                navController.navigate(action)
                            }
                        }
                    }

                }
            }
        }

        binding.signInButton.setOnClickListener {
            viewModel.signIn(
                binding.emailTextFieldLayout.editText?.text.toString(),
                binding.passwordTextFieldLayout.editText?.text.toString()
            )
        }

        binding.forgotPasswordTextView.setOnClickListener {
            val action = LogInFragmentDirections.actionLogInFragmentToForgotPasswordFragment()
            navController.navigate(action)
        }


        binding.emailTextFieldEditText.addTextChangedListener {
            binding.signInButton.isEnabled =
                binding.emailTextFieldLayout.editText?.text?.isNotEmpty() == true &&
                        binding.passwordTextFieldLayout.editText?.text?.isNotEmpty() == true
        }

        binding.passwordTextFieldEditText.addTextChangedListener {
            binding.signInButton.isEnabled =
                binding.emailTextFieldLayout.editText?.text?.isNotEmpty() == true &&
                        binding.passwordTextFieldLayout.editText?.text?.isNotEmpty() == true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}