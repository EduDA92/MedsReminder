package com.example.medsreminder.ui.alarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.medsreminder.ui.alarm.AlarmScheduler

class CancelTakingAlarmReceiver(): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let{

            val alarmScheduler = AlarmScheduler(context)
            val itemTimeHashcode = intent?.getIntExtra(AlarmScheduler.MESSAGE, 0) ?: 0

            if(itemTimeHashcode != 0){
              alarmScheduler.cancel(itemTimeHashcode)
            }

        }
    }
}