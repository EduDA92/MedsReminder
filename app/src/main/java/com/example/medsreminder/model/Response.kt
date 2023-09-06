package com.example.medsreminder.model

sealed class Response<out T> {

    object Loading: Response<Nothing>()
    data class Success<out T>(
        val success: T
    ): Response<T>()

    data class Failure(
        val msg: String
    ): Response<Nothing>()
}
