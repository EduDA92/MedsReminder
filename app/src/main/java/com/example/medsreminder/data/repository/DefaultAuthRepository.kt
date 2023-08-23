package com.example.medsreminder.data.repository

import com.example.medsreminder.model.LoginResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DefaultAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {


    override fun currentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    /* This will execute the task createUser, if there is no exception/error a success response will
    * be returned, if there is an exception a Failure response will be returned. */

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): LoginResponse = try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            LoginResponse.Success(true)
        } catch (e: Exception) {
            LoginResponse.Failure(e.message.toString())
        }



    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): LoginResponse =
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            LoginResponse.Success(true)
        } catch (e: Exception) {
            LoginResponse.Failure(e.message.toString())
        }


    override suspend fun sendPasswordEmail(email: String): LoginResponse =
        try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            LoginResponse.Success(true)
        } catch (e: Exception) {
            LoginResponse.Failure(e.message.toString())
        }


    override fun signOut() = firebaseAuth.signOut()

}