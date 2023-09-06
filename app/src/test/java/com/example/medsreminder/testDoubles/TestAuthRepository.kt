package com.example.medsreminder.testDoubles

import com.example.medsreminder.data.repository.AuthRepository
import com.example.medsreminder.model.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class TestAuthRepository(private val firebaseAuth: FirebaseAuth): AuthRepository {


    override fun currentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Response<Boolean> {
        return if(email.isNotEmpty()  && password.isNotEmpty()){
            Response.Success(true)
        } else {
            Response.Failure("Error")
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Response<Boolean> {
        return if(email.isNotEmpty()  && password.isNotEmpty()){
            Response.Success(true)
        } else {
            Response.Failure("Error")
        }
    }

    override suspend fun sendPasswordEmail(email: String): Response<Boolean> {
        return if(email.isNotEmpty()){
            Response.Success(true)
        } else {
            Response.Failure("Error")
        }
    }

    override fun signOut() {
        println("signOut")
    }


}