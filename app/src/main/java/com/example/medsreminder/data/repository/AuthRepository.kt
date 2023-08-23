package com.example.medsreminder.data.repository

import com.example.medsreminder.model.LoginResponse
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    fun currentUser(): FirebaseUser?

    suspend fun createUserWithEmailAndPassword(email: String, password: String): LoginResponse
    suspend fun signInWithEmailAndPassword(email: String, password: String): LoginResponse
    suspend fun sendPasswordEmail(email: String): LoginResponse

    fun signOut()

}