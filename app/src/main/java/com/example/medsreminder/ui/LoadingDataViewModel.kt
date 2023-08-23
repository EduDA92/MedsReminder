package com.example.medsreminder.ui

import androidx.lifecycle.ViewModel
import com.example.medsreminder.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoadingDataViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {


    private val _loadingState: MutableStateFlow<LoadingUiState> =
        MutableStateFlow(LoadingUiState.Loading)
    val loadingState = _loadingState.asStateFlow()


    fun getCurrentUser(){
        _loadingState.update {
            if (authRepository.currentUser() != null) {
                LoadingUiState.UserAlreadyLogger
            } else {
                LoadingUiState.UserNotLogged
            }
        }
    }

}

sealed interface LoadingUiState {
    object Loading : LoadingUiState
    object UserAlreadyLogger : LoadingUiState
    object UserNotLogged : LoadingUiState
}
