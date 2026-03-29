package com.example.shreetaskmanager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shreetaskmanager.database.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.util.*

class AllTasksActivity : AppCompatActivity() {

    private lateinit var rvAllTasks: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskManager: TaskManager
    private lateinit var chipGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_tasks)

        val db = AppDatabase.getInstance(this)
        taskManager = TaskManager(db.taskDao())

        rvAllTasks = findViewById(R.id.rvAllTasks)
        chipGroup = findViewById(R.id.chipGroup)

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        NavigationUtils.setupBottomNavigation(this, navView, R.id.nav_tasks)

        taskAdapter = TaskAdapter(mutableListOf(), { task ->
            lifecycleScope.launch {
                taskManager.completeTask(task.id)
                refreshData()
            }
        }, { task ->
            // Open task action or edit
            val intent = Intent(this, TaskActionActivity::class.java)
            intent.putExtra("taskId", task.id)
            intent.putExtra("title", task.title)
            startActivity(intent)
        }, { _, _ -> })

        rvAllTasks.layoutManager = LinearLayoutManager(this)
        rvAllTasks.adapter = taskAdapter

        chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipToday -> filterByRange(getTodayRange())
                R.id.chipThisWeek -> filterByRange(getThisWeekRange())
                R.id.chipNextWeek -> filterByRange(getNextWeekRange())
                R.id.chipLastMonth -> filterByRange(getLastMonthRange())
                R.id.chipCurrentMonth -> filterByRange(getCurrentMonthRange())
                R.id.chipNextMonth -> filterByRange(getNextMonthRange())
                R.id.chipYearly -> filterByRange(getYearlyRange())
                R.id.chipCustom -> showCustomRangePicker()
            }
        }

        findViewById<FloatingActionButton>(R.id.btnAnalysisGraph).setOnClickListener {
            startActivity(Intent(this, TaskGraphActivity::class.java))
        }

        // Default filter
        chipGroup.check(R.id.chipToday)
        filterByRange(getTodayRange())
    }

    private fun refreshData() {
        val checkedId = chipGroup.checkedChipId
        if (checkedId != -1) {
             // Re-trigger the logic for the current chip
             when (checkedId) {
                R.id.chipToday -> filterByRange(getTodayRange())
                R.id.chipThisWeek -> filterByRange(getThisWeekRange())
                R.id.chipNextWeek -> filterByRange(getNextWeekRange())
                R.id.chipLastMonth -> filterByRange(getLastMonthRange())
                R.id.chipCurrentMonth -> filterByRange(getCurrentMonthRange())
                R.id.chipNextMonth -> filterByRange(getNextMonthRange())
                R.id.chipYearly -> filterByRange(getYearlyRange())
            }
        }
    }

    private fun filterByRange(range: Pair<Long, Long>) {
        lifecycleScope.launch {
            val tasks = AppDatabase.getInstance(this@AllTasksActivity).taskDao()
                .getTasksInRange(range.first, range.second)
            taskAdapter.update(tasks.map {
                TaskManager.Task(it.id, it.title, it.completed, it.dateMillis, 
                    try { TaskManager.ReminderType.valueOf(it.type) } catch(e:Exception) { TaskManager.ReminderType.GENERIC },
                    it.dueMillis, it.notes, it.phoneNumber, it.estimatedTimeMinutes,
                    try { TaskManager.TaskStatus.valueOf(it.status) } catch(e:Exception) { TaskManager.TaskStatus.PENDING },
                    it.startTimeMillis, it.endTimeMillis)
            })
        }
    }

    private fun showCustomRangePicker() {
        val picker = MaterialDatePicker.Builder.dateRangePicker().build()
        picker.show(supportFragmentManager, "range")
        picker.addOnPositiveButtonClickListener { range ->
            filterByRange(Pair(range.first, range.second + 86400000L - 1L))
        }
    }

    private fun getTodayRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return Pair(start, cal.timeInMillis)
    }

    private fun getThisWeekRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.add(Calendar.DAY_OF_YEAR, 6)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        return Pair(start, cal.timeInMillis)
    }

    private fun getNextWeekRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.add(Calendar.WEEK_OF_YEAR, 1)
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.add(Calendar.DAY_OF_YEAR, 6)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        return Pair(start, cal.timeInMillis)
    }

    private fun getCurrentMonthRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        return Pair(start, cal.timeInMillis)
    }

    private fun getLastMonthRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -1)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        return Pair(start, cal.timeInMillis)
    }

    private fun getNextMonthRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, 1)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        return Pair(start, cal.timeInMillis)
    }

    private fun getYearlyRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_YEAR, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR))
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        return Pair(start, cal.timeInMillis)
    }
}
