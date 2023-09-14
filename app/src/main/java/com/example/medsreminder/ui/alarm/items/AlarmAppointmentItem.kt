package com.example.medsreminder.ui.alarm.items

import java.time.LocalDateTime

data class AlarmAppointmentItem(
    val time: LocalDateTime,
    val msg: String
)
