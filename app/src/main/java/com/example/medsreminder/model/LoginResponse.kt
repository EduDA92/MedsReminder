package com.example.medsreminder.model

sealed interface LoginResponse{

    data class Success(val success: Boolean): LoginResponse

    data class Failure(val msg: String): LoginResponse
}
