package com.example.medsreminder.model


data class Medicine(
    val name: String = "",
    val pillsAmount: Float = 0f,
    val duration: Int = 0,
    val hourSeparation: Int = 0,
    val meal: String = "",
    val creationDate: String = ""
)
