package com.example.medsreminder.ui.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.medsreminder.ui.alarm.items.AlarmTakingItem
import com.example.medsreminder.ui.alarm.receivers.CancelTakingAlarmReceiver
import com.example.medsreminder.ui.alarm.receivers.TakingAlarmReceiver
import java.time.Duration
import java.time.ZoneId

class AlarmScheduler(private val context: Context)  {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("MissingPermission")
    fun schedule(item: AlarmTakingItem) {

        val intent = Intent(context, TakingAlarmReceiver::class.java).apply {
            putExtra(MESSAGE, item.msg)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            item.interval,
            PendingIntent.getBroadcast(
                context,
                item.time.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        )

    }


    @SuppressLint("MissingPermission")
    fun scheduleCancelAlarm(item: AlarmTakingItem) {
        val intent = Intent(context, CancelTakingAlarmReceiver::class.java).apply {
            putExtra(MESSAGE, item.time.hashCode())
        }

        // add half hour to the last taking so the notification for the last item will take place
        val temporalAmount = Duration.ofHours(item.duration.times(24).plus(0.5).toLong())

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.time.plus(temporalAmount).atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                item.time.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    fun cancel(itemTimeHashcode : Int) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                itemTimeHashcode,
                Intent(context, TakingAlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    companion object{
        const val MESSAGE = "Message"
    }

}