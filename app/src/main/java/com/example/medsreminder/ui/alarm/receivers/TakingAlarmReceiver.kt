package com.example.medsreminder.ui.alarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.medsreminder.ui.alarm.AlarmScheduler
import com.example.medsreminder.ui.alarm.TakingsNotification

class TakingAlarmReceiver(): BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {


        val message = intent?.getStringExtra(AlarmScheduler.MESSAGE) ?: return

        context?.let{
            val takingsNotification = TakingsNotification(it)
            takingsNotification.showNotification(message)
        }

        }


    }
