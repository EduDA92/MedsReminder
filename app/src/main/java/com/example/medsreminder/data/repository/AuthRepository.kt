package com.example.medsreminder.data.repository

import com.example.medsreminder.model.Response
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    fun currentUser(): FirebaseUser?

    suspend fun createUserWithEmailAndPassword(email: String, password: String): Response<Boolean>
    suspend fun signInWithEmailAndPassword(email: String, password: String): Response<Boolean>
    suspend fun sendPasswordEmail(email: String): Response<Boolean>

    fun signOut()

}