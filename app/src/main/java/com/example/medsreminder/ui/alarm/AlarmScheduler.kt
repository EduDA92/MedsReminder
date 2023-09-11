package com.example.medsreminder.ui.alarm

interface AlarmScheduler {
    fun schedule(item: AlarmTakingItem)

    fun scheduleCancelAlarm(item: AlarmTakingItem)
    fun cancel(itemTimeHashcode : Int)
}