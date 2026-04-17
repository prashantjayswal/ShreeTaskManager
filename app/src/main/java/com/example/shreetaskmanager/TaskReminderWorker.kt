package com.example.shreetaskmanager

import android.content.Context
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class TaskReminderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val taskId = inputData.getLong("taskId", -1L)
        val title = inputData.getString("title") ?: "Task Reminder"
        val message = inputData.getString("message") ?: "Task is starting"
        
        if (taskId != -1L) {
            showNotification(taskId, title, message)
        }
        
        return Result.success()
    }

    private fun showNotification(id: Long, title: String, message: String) {
        val sharedPref = applicationContext.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val alarmEnabled = sharedPref.getBoolean("alarm_enabled", false)

        val builder = NotificationCompat.Builder(applicationContext, "default")
            .setSmallIcon(R.drawable.ic_task)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        if (alarmEnabled) {
            val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM) 
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            builder.setSound(alarmSound)
            // For older Android or when channel doesn't force sound
            builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE)
        }

        try {
            val notificationId = (id.toInt() * 31) + message.hashCode()
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(notificationId, builder.build())
            }
        } catch (e: SecurityException) {
            // Permission missing
        }
    }
}
