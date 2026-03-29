package com.example.shreetaskmanager

import android.content.Context
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
        val builder = NotificationCompat.Builder(applicationContext, "default")
            .setSmallIcon(R.drawable.ic_task)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        try {
            // Use a combination of taskId and message hash to avoid overwriting if both show up
            val notificationId = (id.toInt() * 31) + message.hashCode()
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(notificationId, builder.build())
            }
        } catch (e: SecurityException) {
            // Log error or handle missing permission
        }
    }
}
