package com.example.medsreminder.ui.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.medsreminder.R
import com.example.medsreminder.ui.MainActivity

class TakingsNotification(private val context: Context) {

    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    fun showNotification(msg: String){

        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.appicon)
            .setContentTitle(context.getString(R.string.taking_notification_title))
            .setContentText(msg)
            .setContentIntent(activityPendingIntent)


        notificationManager.notify(1, notification.build())

    }

    companion object{
        const val CHANNEL_ID = "Takings_channel"
    }
}