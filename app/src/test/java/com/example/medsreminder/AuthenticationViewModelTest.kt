package com.example.medsreminder

import app.cash.turbine.test
import com.example.medsreminder.data.repository.AuthRepository
import com.example.medsreminder.repositories.TestAuthRepository
import com.example.medsreminder.ui.authentication.AuthUiState
import com.example.medsreminder.ui.authentication.AuthenticationViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals

class AuthenticationViewModelTest {


    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val testEmail = "test@test.con"
    private val testEmailPass = "testing"


    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authRepository: AuthRepository
    private lateinit var subject: AuthenticationViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        Dispatchers.setMain(mainThreadSurrogate)

        firebaseAuth = Mockito.mock(FirebaseAuth::class.java)
        authRepository = TestAuthRepository(firebaseAuth)
        subject = AuthenticationViewModel(authRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun authenticationViewModel_createUser_successfully_changes_state_to_LoginSuccess() = runTest {
        subject.signUpResponse.test {
            subject.createUser(testEmail, testEmailPass)
            assertEquals(AuthUiState.LoginSuccess(true), awaitItem())
        }
    }

    @Test
    fun authenticationViewModel_createUser_bad_data_changes_state_to_LoginError() = runTest{
        subject.signUpResponse.test{
            subject.createUser("", "")
            assertEquals(AuthUiState.LoginError("Error"), awaitItem())
        }
    }

    @Test
    fun authenticationViewModel_signIn_successfully_changes_state_to_LoginSuccess() = runTest {
        subject.signInResponse.test {
            subject.signIn(testEmail, testEmailPass)
            assertEquals(AuthUiState.LoginSuccess(true), awaitItem())
        }
    }

    @Test
    fun authenticationViewModel_signIn_bad_data_changes_state_to_LoginError() = runTest{
        subject.signInResponse.test{
            subject.signIn("", "")
            assertEquals(AuthUiState.LoginError("Error"), awaitItem())
        }
    }

    @Test
    fun authenticationViewModel_sendPasswordEmail_successfully_changes_state_to_LoginSuccess() = runTest {
        subject.passwordEmailResponse.test {
            subject.sendPasswordEmail(testEmail)
            assertEquals(AuthUiState.LoginSuccess(true), awaitItem())
        }
    }

    @Test
    fun authenticationViewModel_sendPasswordEmail_bad_data_changes_state_to_LoginError() = runTest{
        subject.passwordEmailResponse.test{
            subject.sendPasswordEmail("")
            assertEquals(AuthUiState.LoginError("Error"), awaitItem())
        }
    }


}