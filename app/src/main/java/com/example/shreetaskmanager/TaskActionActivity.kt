package com.example.shreetaskmanager

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shreetaskmanager.database.AppDatabase
import kotlinx.coroutines.launch

class TaskActionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_action)

        val taskId = intent.getLongOf("taskId", -1)
        val title = intent.getStringExtra("title") ?: "Task"

        findViewById<TextView>(R.id.tvTaskTitle).text = title

        findViewById<Button>(R.id.btnStartTask).setOnClickListener {
            lifecycleScope.launch {
                val db = AppDatabase.getInstance(this@TaskActionActivity)
                val dao = db.taskDao()
                val task = dao.getById(taskId)
                if (task != null) {
                    dao.update(task.copy(status = TaskManager.TaskStatus.IN_PIPELINE.name))
                }
                finish()
            }
        }

        findViewById<Button>(R.id.btnReschedule).setOnClickListener {
            // For simplicity, move to tomorrow
            lifecycleScope.launch {
                val db = AppDatabase.getInstance(this@TaskActionActivity)
                val dao = db.taskDao()
                val task = dao.getById(taskId)
                if (task != null) {
                    val nextDay = task.dateMillis + 86400000L
                    dao.update(task.copy(dateMillis = nextDay, status = TaskManager.TaskStatus.RESCHEDULED.name))
                }
                finish()
            }
        }
    }
}

private fun android.content.Intent.getLongOf(key: String, defaultValue: Long): Long {
    return this.getLongExtra(key, defaultValue)
}
