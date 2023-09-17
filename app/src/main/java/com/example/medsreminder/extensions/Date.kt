package com.example.medsreminder.extensions

import java.time.Duration
import java.time.LocalDate

fun Long.toDate(): LocalDate {
    return LocalDate.ofEpochDay(Duration.ofMillis(this).toDays())
}