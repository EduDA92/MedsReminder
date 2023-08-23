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
import com.example.medsreminder.databinding.FragmentForgotPasswordBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthenticationViewModel by hiltNavGraphViewModels(R.id.auth)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* setting up the topAppBar */
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.forgotPasswordTopAppBar.setupWithNavController(navController, appBarConfiguration)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.passwordEmailResponse.collect{uiState ->

                    when(uiState){
                        is AuthUiState.LoginError -> Snackbar.make(binding.root, uiState.msg, Snackbar.LENGTH_SHORT)
                            .show()
                        is AuthUiState.LoginSuccess -> {
                            if(uiState.success){
                                Snackbar.make(binding.root, getString(R.string.email_sent), Snackbar.LENGTH_SHORT)
                                    .show()
                                navController.popBackStack(R.id.logInFragment, false)
                            }
                        }
                    }

                }
            }
        }


        binding.sendEmailButton.setOnClickListener {
            viewModel.sendPasswordEmail(binding.emailTextFieldLayout.editText?.text.toString())
        }


        /* enable/disable button */
        binding.emailTextFieldEditText.addTextChangedListener {
            binding.sendEmailButton.isEnabled = binding.emailTextFieldLayout.editText?.text?.isNotEmpty() == true
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
