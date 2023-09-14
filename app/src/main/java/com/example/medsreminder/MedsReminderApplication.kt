package com.example.medsreminder

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.example.medsreminder.ui.alarm.AppointmentNotification
import com.example.medsreminder.ui.alarm.TakingsNotification
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MedsReminderApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        createAppointmentNotificationChannel()
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val CHANNEL_ID = TakingsNotification.CHANNEL_ID
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createAppointmentNotificationChannel(){
        val name = getString(R.string.appointments_channel_name)
        val descriptionText = getString(R.string.appointments_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val CHANNEL_ID = AppointmentNotification.APPOINTMENT_CHANNEL_ID
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}