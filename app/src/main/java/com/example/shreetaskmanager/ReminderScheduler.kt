package com.example.shreetaskmanager

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object ReminderScheduler {
    fun scheduleTaskReminder(context: Context, taskId: Long, title: String, startTimeMillis: Long) {
        val workManager = WorkManager.getInstance(context)

        // 1. Schedule "Starting now" reminder
        val nowDelay = startTimeMillis - System.currentTimeMillis()
        if (nowDelay > 0) {
            val nowData = Data.Builder()
                .putLong("taskId", taskId)
                .putString("title", title)
                .putString("message", "Task is starting now")
                .build()

            val nowRequest = OneTimeWorkRequestBuilder<TaskReminderWorker>()
                .setInitialDelay(nowDelay, TimeUnit.MILLISECONDS)
                .setInputData(nowData)
                .addTag("task_${taskId}_now")
                .build()

            workManager.enqueueUniqueWork(
                "task_reminder_${taskId}_now",
                ExistingWorkPolicy.REPLACE,
                nowRequest
            )
        }

        // 2. Schedule "15 min before" reminder
        val fifteenMinBeforeMillis = startTimeMillis - TimeUnit.MINUTES.toMillis(15)
        val beforeDelay = fifteenMinBeforeMillis - System.currentTimeMillis()
        if (beforeDelay > 0) {
            val beforeData = Data.Builder()
                .putLong("taskId", taskId)
                .putString("title", title)
                .putString("message", "Task starts in 15 minutes")
                .build()

            val beforeRequest = OneTimeWorkRequestBuilder<TaskReminderWorker>()
                .setInitialDelay(beforeDelay, TimeUnit.MILLISECONDS)
                .setInputData(beforeData)
                .addTag("task_${taskId}_before")
                .build()

            workManager.enqueueUniqueWork(
                "task_reminder_${taskId}_before",
                ExistingWorkPolicy.REPLACE,
                beforeRequest
            )
        }
    }

    fun cancelReminder(context: Context, taskId: Long) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork("task_reminder_${taskId}_now")
        workManager.cancelUniqueWork("task_reminder_${taskId}_before")
    }
}
