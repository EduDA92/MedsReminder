package com.example.medsreminder.model



data class Taking(
    val medicineName: String = "",
    val date: String = "",
    val hour: Int = 0,
    val minute: Int = 0,
    val pillNumber:Float = 0f,
    val status: String = ""
)
