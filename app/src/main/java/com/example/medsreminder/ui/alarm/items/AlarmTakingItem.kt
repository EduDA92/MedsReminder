package com.example.medsreminder.ui.alarm.items

import java.time.LocalDateTime

data class AlarmTakingItem(
     val time: LocalDateTime,
     val interval: Long,
     val duration: Int,
     val msg: String
)
