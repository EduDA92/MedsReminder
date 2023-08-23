package com.example.medsreminder.data.repository.fake

import com.example.medsreminder.data.repository.AuthRepository
import com.example.medsreminder.model.LoginResponse
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

/* Fake repo to test navigation with it, just returns success if there is an input otherwise returns exception */

class FakeAuthRepository@Inject constructor(): AuthRepository {

    override fun currentUser(): FirebaseUser? {
        return null
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): LoginResponse{
        return if(email.isNotEmpty()  && password.isNotEmpty()){
            LoginResponse.Success(true)
        } else {
            LoginResponse.Failure("Error")
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): LoginResponse{
        return if(email.isNotEmpty()  && password.isNotEmpty()){
            LoginResponse.Success(true)
        } else {
            LoginResponse.Failure("Error")
        }
    }

    override suspend fun sendPasswordEmail(email: String): LoginResponse{
        return if(email.isNotEmpty()){
            LoginResponse.Success(true)
        } else {
            LoginResponse.Failure("Error")
        }
    }

    override fun signOut() {
        println("signOut")
    }
}