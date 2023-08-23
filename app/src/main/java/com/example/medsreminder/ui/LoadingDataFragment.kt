package com.example.medsreminder.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.medsreminder.databinding.FragmentLoadingDataBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoadingDataFragment : Fragment() {

    private var _binding: FragmentLoadingDataBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoadingDataViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoadingDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        /* Update current user data */
        viewModel.getCurrentUser()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.loadingState.collect{ state ->
                    when(state){
                        LoadingUiState.Loading -> binding.loadingDataIndicator.visibility = View.VISIBLE
                        LoadingUiState.UserAlreadyLogger -> {
                            binding.loadingDataIndicator.visibility = View.INVISIBLE
                            val action = LoadingDataFragmentDirections.actionLoadingDataFragmentToDashboard()
                            navController.navigate(action)
                        }
                        LoadingUiState.UserNotLogged -> {
                            binding.loadingDataIndicator.visibility = View.INVISIBLE
                            val action = LoadingDataFragmentDirections.actionLoadingDataFragmentToAuth()
                            navController.navigate(action)
                        }
                    }


                }

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}