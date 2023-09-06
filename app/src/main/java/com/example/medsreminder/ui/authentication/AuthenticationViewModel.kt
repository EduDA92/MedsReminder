package com.example.medsreminder.ui.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medsreminder.data.repository.AuthRepository
import com.example.medsreminder.model.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    /* As the response should be one time only events SharedFlow suits better this scenario, with StateFLow
    * the state was retained when navigating from screen to screen and the error snackbars were showing again
    * when navigating back to the login fragments*/

    private val _signUpResponse = MutableSharedFlow<AuthUiState>(replay = 0)
    val signUpResponse = _signUpResponse.asSharedFlow()

    private val _signInResponse = MutableSharedFlow<AuthUiState>(replay = 0)
    val signInResponse = _signInResponse.asSharedFlow()

    private val _passwordEmailResponse = MutableSharedFlow<AuthUiState>(replay = 0)
    val passwordEmailResponse = _passwordEmailResponse.asSharedFlow()

    fun createUser( email: String, password: String) {
        viewModelScope.launch {
            when (val response = authRepository.createUserWithEmailAndPassword(email, password)) {
                is Response.Failure -> _signUpResponse.emit(AuthUiState.LoginError(response.msg))
                is Response.Success -> _signUpResponse.emit(AuthUiState.LoginSuccess(response.success))
                Response.Loading -> {/* Nothing */}
            }
        }
    }


    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            when (val response = authRepository.signInWithEmailAndPassword(email, password)) {
                is Response.Failure -> _signInResponse.emit(AuthUiState.LoginError(response.msg))
                is Response.Success -> _signInResponse.emit(AuthUiState.LoginSuccess(response.success))
                Response.Loading -> {/* Nothing */}
            }
        }
    }

    fun sendPasswordEmail(email: String) {
        viewModelScope.launch {
            when (val response = authRepository.sendPasswordEmail(email)) {
                is Response.Failure -> _passwordEmailResponse.emit(AuthUiState.LoginError(response.msg))
                is Response.Success -> _passwordEmailResponse.emit(AuthUiState.LoginSuccess(response.success))
                Response.Loading -> {/* Nothing */}
            }
        }
    }

}

sealed interface AuthUiState {
    data class LoginSuccess(val success: Boolean) : AuthUiState
    data class LoginError(val msg: String) : AuthUiState
}