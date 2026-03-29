package com.example.shreetaskmanager

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object ReminderManager {
    fun scheduleTaskReminder(context: Context, task: TaskManager.Task) {
        if (task.startTimeMillis <= System.currentTimeMillis()) return

        val delay = task.startTimeMillis - System.currentTimeMillis()
        val data = Data.Builder()
            .putLong("taskId", task.id)
            .putString("title", task.title)
            .build()

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag("task_${task.id}")
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }

    fun cancelReminder(context: Context, taskId: Long) {
        WorkManager.getInstance(context).cancelAllWorkByTag("task_$taskId")
    }
}
