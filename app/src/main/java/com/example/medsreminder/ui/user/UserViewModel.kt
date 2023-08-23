package com.example.medsreminder.ui.user

import androidx.lifecycle.ViewModel
import com.example.medsreminder.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    val userData = authRepository.currentUser()

    fun signOut() = authRepository.signOut()

}