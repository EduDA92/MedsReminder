package com.example.medsreminder.data.repository

import com.example.medsreminder.model.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
    ): Response<Boolean> = try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e.message.toString())
        }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Response<Boolean>  =
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e.message.toString())
        }


    override suspend fun sendPasswordEmail(email: String): Response<Boolean>  =
        try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e.message.toString())
        }


    override fun signOut() = firebaseAuth.signOut()

}