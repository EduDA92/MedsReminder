package com.example.medsreminder.ui.alarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.medsreminder.R
import com.example.medsreminder.data.repository.MedicineTakingRepository
import com.example.medsreminder.ui.alarm.AlarmScheduler
import com.example.medsreminder.ui.alarm.items.AlarmTakingItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class TakingAlarmBootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: MedicineTakingRepository
    override fun onReceive(context: Context?, intent: Intent?) {

        context?.let {
            val takingAlarmScheduler = AlarmScheduler(it)

            if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
                CoroutineScope(Dispatchers.IO).launch {
                    val documents = repository.getMedicineTakings()
                    documents.collect { takings ->
                        takings.forEach { medicineTaking ->
                            val takingItem = AlarmTakingItem(
                                time = LocalDateTime.parse(medicineTaking.medicine!!.creationDate),
                                duration = medicineTaking.medicine.duration,
                                interval = medicineTaking.medicine.hourSeparation.toMilis(),
                                msg = context.getString(
                                    R.string.notification_taking_reminder,
                                    medicineTaking.medicine.name
                                )
                            )
                            takingAlarmScheduler.schedule(takingItem)
                            takingAlarmScheduler.scheduleCancelAlarm(takingItem)
                        }
                    }
                }
            }
        }

    }

    private fun Int.toMilis(): Long {
        return this.times(60).times(60).times(1000).toLong()
    }
}