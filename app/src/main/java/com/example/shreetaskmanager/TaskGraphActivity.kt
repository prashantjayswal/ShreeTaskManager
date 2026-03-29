package com.example.shreetaskmanager

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shreetaskmanager.database.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class TaskGraphActivity : AppCompatActivity() {

    private lateinit var taskManager: TaskManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_graph)

        val db = AppDatabase.getInstance(this)
        taskManager = TaskManager(db.taskDao())

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        // I need to add this to the menu if I want it in bottom nav, 
        // but user asked for "Analysis of all expense" earlier and now "graph of all task".
        // I'll reuse the 'Analysis' slot or add a new one if possible.
        // For now, let's just setup the graph data.
        
        loadGraphData()
    }

    private fun loadGraphData() {
        lifecycleScope.launch {
            val allTasks = taskManager.getAllTasks()
            val total = allTasks.size.coerceAtLeast(1)

            val completed = allTasks.count { it.status == TaskManager.TaskStatus.COMPLETED }
            val pending = allTasks.count { it.status == TaskManager.TaskStatus.PENDING }
            val rescheduled = allTasks.count { it.status == TaskManager.TaskStatus.RESCHEDULED }
            val pipeline = allTasks.count { it.status == TaskManager.TaskStatus.IN_PIPELINE }

            findViewById<ProgressBar>(R.id.pbCompleted).apply {
                max = total
                progress = completed
            }
            findViewById<ProgressBar>(R.id.pbPending).apply {
                max = total
                progress = pending
            }
            findViewById<ProgressBar>(R.id.pbRescheduled).apply {
                max = total
                progress = rescheduled
            }
            findViewById<ProgressBar>(R.id.pbPipeline).apply {
                max = total
                progress = pipeline
            }
        }
    }
}
