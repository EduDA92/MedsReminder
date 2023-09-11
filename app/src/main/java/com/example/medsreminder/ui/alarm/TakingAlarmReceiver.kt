package com.example.medsreminder.ui.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TakingAlarmReceiver(): BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {


        val message = intent?.getStringExtra(TakingAlarmScheduler.MESSAGE) ?: return

        context?.let{
            val takingsNotification = TakingsNotification(it)
            takingsNotification.showNotification(message)
        }

        }


    }
