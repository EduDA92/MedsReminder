package com.example.medsreminder.repositories

import com.example.medsreminder.data.repository.AuthRepository
import com.example.medsreminder.model.LoginResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser

class TestAuthRepository(private val firebaseAuth: FirebaseAuth): AuthRepository {


    override fun currentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): LoginResponse {
        return if(email.isNotEmpty()  && password.isNotEmpty()){
            LoginResponse.Success(true)
        } else {
            LoginResponse.Failure("Error")
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): LoginResponse {
        return if(email.isNotEmpty()  && password.isNotEmpty()){
            LoginResponse.Success(true)
        } else {
            LoginResponse.Failure("Error")
        }
    }

    override suspend fun sendPasswordEmail(email: String): LoginResponse {
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