package com.example.medsreminder

import com.example.medsreminder.data.repository.DefaultAuthRepository
import com.example.medsreminder.model.LoginResponse
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import kotlin.test.assertEquals


class DefaultAuthRepositoryTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testScope = TestScope(UnconfinedTestDispatcher())

    private val testEmail = "test@test.con"
    private val testEmailPass = "testing"

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authResult: AuthResult
    private lateinit var subject: DefaultAuthRepository

    @Before
    fun setup() {

        firebaseAuth = mock(FirebaseAuth::class.java)
        authResult = mock(AuthResult::class.java)
        subject = DefaultAuthRepository(firebaseAuth)
    }

    @Test
    fun defaultAuthRepository_Sign_In_With_Email_Password_Returns_Success() =
        testScope.runTest {
            Mockito.`when`(firebaseAuth.signInWithEmailAndPassword(testEmail, testEmailPass))
                .thenReturn(
                    Tasks.forResult(authResult)
                )
            assertEquals(
                LoginResponse.Success(true),
                subject.signInWithEmailAndPassword(testEmail, testEmailPass)
            )
        }



    @Test
    fun defaultAuthRepository_Sign_Up_With_Email_Password_Returns_Success() =
        testScope.runTest {
            Mockito.`when`(firebaseAuth.createUserWithEmailAndPassword(testEmail, testEmailPass))
                .thenReturn(
                    Tasks.forResult(authResult)
                )
            assertEquals(
                LoginResponse.Success(true),
                subject.createUserWithEmailAndPassword(testEmail, testEmailPass)
            )
        }

    @Test
    fun defaultAuthRepository_Sign_In_With_Bad_Data_Returns_Error() =
        testScope.runTest {
            Mockito.`when`(firebaseAuth.signInWithEmailAndPassword(testEmail, ""))
                .thenThrow(RuntimeException("Error"))
            assertEquals(
                LoginResponse.Failure("Error"),
                subject.signInWithEmailAndPassword(testEmail,"")
            )
        }

    @Test
    fun defaultAuthRepository_Sign_Up_With_Bad_Data_Returns_Error() =
        testScope.runTest {
            Mockito.`when`(firebaseAuth.createUserWithEmailAndPassword(testEmail, ""))
                .thenThrow(RuntimeException("Error"))
            assertEquals(
                LoginResponse.Failure("Error"),
                subject.createUserWithEmailAndPassword(testEmail,"")
            )
        }

    @Test
    fun defaultAuthRepository_Valid_Email_For_Password_Recovery_Returns_Success() =
        testScope.runTest {
            Mockito.`when`(firebaseAuth.sendPasswordResetEmail(testEmail))
                .thenAnswer {Tasks.forResult(Unit)}
            assertEquals(
                LoginResponse.Success(true),
                subject.sendPasswordEmail(testEmail)
            )
        }


    @Test
    fun defaultAuthRepository_Invalid_Email_For_Password_Recovery_Returns_Success() =
        testScope.runTest {

            Mockito.`when`(firebaseAuth.sendPasswordResetEmail(""))
                .thenThrow(RuntimeException("Error"))
            assertEquals(
                LoginResponse.Failure("Error"),
                subject.sendPasswordEmail("")
            )

        }
}
