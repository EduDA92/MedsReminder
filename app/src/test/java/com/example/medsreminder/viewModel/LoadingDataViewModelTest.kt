package com.example.medsreminder.viewModel

import app.cash.turbine.test
import com.example.medsreminder.data.repository.AuthRepository
import com.example.medsreminder.testDoubles.TestAuthRepository
import com.example.medsreminder.ui.LoadingDataViewModel
import com.example.medsreminder.ui.LoadingUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals

class LoadingDataViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("LoadingDataViewModelTest thread")


    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var subject: LoadingDataViewModel
    private lateinit var authRepository: AuthRepository


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)

        user = Mockito.mock(FirebaseUser::class.java)
        firebaseAuth = Mockito.mock(FirebaseAuth::class.java)
        authRepository = TestAuthRepository(firebaseAuth)
        subject = LoadingDataViewModel(authRepository)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun loadingDataViewModel_if_null_user_UserNotLogged_state() = runTest {

        Mockito.`when`(firebaseAuth.currentUser).thenReturn(null)
        subject.getCurrentUser()

       subject.loadingState.test {
            assertEquals(LoadingUiState.UserNotLogged, awaitItem())
        }
    }

    @Test
    fun loadingDataViewModel_if_user_UserAlreadyLogged_state() = runTest {

        Mockito.`when`(firebaseAuth.currentUser).thenReturn(user)
        subject.getCurrentUser()

        subject.loadingState.test {
            assertEquals(LoadingUiState.UserAlreadyLogger, awaitItem())
        }

    }

}