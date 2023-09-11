package com.example.medsreminder.ui.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class CancelTakingAlarmReceiver(): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let{

            val alarmScheduler = TakingAlarmScheduler(context)
            val itemTimeHashcode = intent?.getIntExtra(TakingAlarmScheduler.MESSAGE, 0) ?: 0

            if(itemTimeHashcode != 0){
              alarmScheduler.cancel(itemTimeHashcode)
            }

        }
    }
}