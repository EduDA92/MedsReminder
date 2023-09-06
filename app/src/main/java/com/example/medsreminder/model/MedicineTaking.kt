package com.example.medsreminder.model

data class MedicineTaking(
    val medicine: Medicine? = null,
    val takings: List<Taking> = emptyList()
)
