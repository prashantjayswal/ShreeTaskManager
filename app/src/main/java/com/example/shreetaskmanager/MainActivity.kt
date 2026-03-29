package com.example.shreetaskmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.text.InputType
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.app.Activity
import android.widget.AdapterView
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.os.Build

class MainActivity : AppCompatActivity() {
    private lateinit var taskManager: TaskManager
    private val gson = Gson()
    private lateinit var expenseManager: ExpenseManager
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var dateAdapter: DateAdapter
    
    private lateinit var tvMonth: TextView
    private lateinit var tvDailySummary: TextView
    private lateinit var rvDates: RecyclerView
    private lateinit var recyclerTasks: RecyclerView
    private lateinit var recyclerExpenses: RecyclerView
    private lateinit var buttonAdd: View

    private var selectedDateMillis: Long = 0

    // Hidden/Original components preserved for logic
    private lateinit var calendarView: android.widget.CalendarView
    private lateinit var buttonSignIn: android.widget.Button
    private lateinit var buttonBackup: android.widget.Button
    private lateinit var buttonRestore: android.widget.Button
    
    private lateinit var googleSignInClient: com.google.android.gms.auth.api.signin.GoogleSignInClient
    private val RC_SIGN_IN = 9001
    private val RC_PICK_CONTACT = 9002
    private val RC_NOTIFICATION_PERMISSION = 9003

    private var currentAddTaskPhoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvMonth = findViewById(R.id.tvMonth)
        tvDailySummary = findViewById(R.id.tvDailySummary)
        rvDates = findViewById(R.id.rvDates)
        recyclerTasks = findViewById(R.id.recyclerTasks)
        recyclerExpenses = findViewById(R.id.recyclerExpenses)
        buttonAdd = findViewById(R.id.buttonAdd)
        
        // Hidden but needed by existing code
        calendarView = findViewById(R.id.calendarView)
        buttonSignIn = findViewById(R.id.buttonSignIn)
        buttonBackup = findViewById(R.id.buttonBackup)
        buttonRestore = findViewById(R.id.buttonRestore)

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        NavigationUtils.setupBottomNavigation(this, navView, R.id.nav_calendar)

        selectedDateMillis = normalizeDate(System.currentTimeMillis())
        updateMonthText(selectedDateMillis)

        setupGoogleSignIn()
        createNotificationChannel()
        checkNotificationPermission()

        val db = com.example.shreetaskmanager.database.AppDatabase.getInstance(this)
        taskManager = TaskManager(db.taskDao())
        expenseManager = ExpenseManager(db.expenseDao())

        setupDateRecyclerView()
        setupTaskRecyclerView()
        setupExpenseRecyclerView()

        buttonAdd.setOnClickListener {
            showAddDialog()
        }

        // Allow clicking month to select Year/Month/Day
        findViewById<View>(R.id.layoutMonth).setOnClickListener {
            showDatePicker()
        }

        // initial load
        loadDataFor(selectedDateMillis)
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), RC_NOTIFICATION_PERMISSION)
            }
        }
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        cal.timeInMillis = selectedDateMillis
        val dpd = android.app.DatePickerDialog(this,
            { _, year, month, dayOfMonth ->
                val newCal = Calendar.getInstance()
                newCal.set(year, month, dayOfMonth)
                selectedDateMillis = normalizeDate(newCal.timeInMillis)
                updateMonthText(selectedDateMillis)
                setupDateRecyclerView() // Refresh horizontal dates
                loadDataFor(selectedDateMillis)
            },
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        )
        dpd.show()
    }

    private fun setupDateRecyclerView() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedDateMillis
        calendar.add(Calendar.DAY_OF_YEAR, -15)
        
        val dateList = mutableListOf<Long>()
        for (i in 0..30) {
            dateList.add(calendar.timeInMillis)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        dateAdapter = DateAdapter(dateList, selectedDateMillis) { date ->
            selectedDateMillis = normalizeDate(date)
            updateMonthText(selectedDateMillis)
            loadDataFor(selectedDateMillis)
        }
        rvDates.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvDates.adapter = dateAdapter
        rvDates.scrollToPosition(15)
    }

    private fun setupTaskRecyclerView() {
        taskAdapter = TaskAdapter(mutableListOf(), { task ->
            lifecycleScope.launch {
                taskManager.completeTask(task.id)
                ReminderScheduler.cancelReminder(this@MainActivity, task.id)
                loadDataFor(selectedDateMillis)
            }
        }, { task ->
            showAddTaskDialog(task) // Edit mode
        }, { task, view ->
            showTaskOptions(task, view)
        })

        recyclerTasks.layoutManager = LinearLayoutManager(this)
        recyclerTasks.adapter = taskAdapter
    }

    private fun setupExpenseRecyclerView() {
        expenseAdapter = ExpenseAdapter(mutableListOf()) { record ->
            showAddExpenseDialog(record.isInvestment, record)
        }
        recyclerExpenses.layoutManager = LinearLayoutManager(this)
        recyclerExpenses.adapter = expenseAdapter
    }

    private fun showTaskOptions(task: TaskManager.Task, view: View) {
        val popup = androidx.appcompat.widget.PopupMenu(this, view)
        if (task.type == TaskManager.ReminderType.CALL && !task.phoneNumber.isNullOrBlank()) {
            popup.menu.add("Call").setOnMenuItemClickListener {
                initiateCall(task)
                true
            }
        }
        popup.menu.add("Edit").setOnMenuItemClickListener {
            showAddTaskDialog(task)
            true
        }
        popup.menu.add("Reschedule").setOnMenuItemClickListener {
            showRescheduleDialog(task)
            true
        }
        popup.menu.add("Complete").setOnMenuItemClickListener {
            lifecycleScope.launch {
                taskManager.completeTask(task.id)
                ReminderScheduler.cancelReminder(this@MainActivity, task.id)
                loadDataFor(selectedDateMillis)
            }
            true
        }
        popup.show()
    }

    private fun updateMonthText(millis: Long) {
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        tvMonth.text = sdf.format(Date(millis))
    }

    private fun normalizeDate(date: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = date
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun loadDataFor(dayMillis: Long) {
        lifecycleScope.launch {
            val tasks = taskManager.tasksForDay(dayMillis)
            taskAdapter.update(tasks)
            
            val expenses = expenseManager.expensesForDay(dayMillis)
            expenseAdapter.update(expenses)

            // Update Daily Financial Summary
            var spent = 0.0
            var invested = 0.0
            expenses.forEach { 
                if (it.isInvestment) invested += it.amount else spent += it.amount
            }
            tvDailySummary.text = "Spent: ₹${spent.toInt()} | Inv: ₹${invested.toInt()}"
        }
    }

    private fun initiateCall(task: TaskManager.Task) {
        val phone = task.phoneNumber ?: return
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        startActivity(intent)
        lifecycleScope.launch {
            taskManager.completeTask(task.id)
            ReminderScheduler.cancelReminder(this@MainActivity, task.id)
            loadDataFor(selectedDateMillis)
        }
    }

    private fun showRescheduleDialog(task: TaskManager.Task) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            val dateBtn = Button(context).apply { text = "Change date" }
            val noteInput = EditText(context).apply { hint = "Reason for delay (optional)" }
            addView(dateBtn)
            addView(noteInput)
            dateBtn.setOnClickListener {
                val cal = Calendar.getInstance().apply { timeInMillis = task.dateMillis }
                val dpd = android.app.DatePickerDialog(this@MainActivity,
                    { _, y, m, d ->
                        cal.set(y, m, d, 0, 0, 0)
                        dateBtn.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                    },
                    cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                dpd.show()
            }
        }
        AlertDialog.Builder(this)
            .setTitle("Reschedule task")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val dateButton = layout.getChildAt(0) as Button
                val newDateText = dateButton.text.toString()
                val note = (layout.getChildAt(1) as EditText).text.toString().takeIf { it.isNotBlank() }
                val newDateMillis = try {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(newDateText)?.time ?: task.dateMillis
                } catch (e: Exception) {
                    task.dateMillis
                }
                lifecycleScope.launch {
                    taskManager.moveTask(task.id, newDateMillis, note)
                    // If we move the task, we should reschedule the reminder
                    val updatedTask = taskManager.tasksForDay(newDateMillis).find { it.id == task.id }
                    if (updatedTask != null && updatedTask.startTimeMillis > 0) {
                        ReminderScheduler.scheduleTaskReminder(this@MainActivity, updatedTask.id, updatedTask.title, updatedTask.startTimeMillis)
                    } else {
                        ReminderScheduler.cancelReminder(this@MainActivity, task.id)
                    }
                    loadDataFor(selectedDateMillis)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupGoogleSignIn() {
        val clientId = getString(R.string.default_web_client_id)
        val gso = com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(
            com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestEmail()
            .requestIdToken(clientId)
            .requestScopes(com.google.android.gms.common.api.Scope("https://www.googleapis.com/auth/drive.file"))
            .build()
        googleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
                android.widget.Toast.makeText(this, "Signed in: ${account?.email}", android.widget.Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                val statusCode = (e as? com.google.android.gms.common.api.ApiException)?.statusCode
                android.widget.Toast.makeText(this, "Sign-in failed (status: $statusCode)", android.widget.Toast.LENGTH_LONG).show()
            }
        } else if (requestCode == RC_PICK_CONTACT && resultCode == Activity.RESULT_OK) {
            data?.data?.let { contactUri ->
                val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                contentResolver.query(contactUri, projection, null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val number = cursor.getString(0)
                        val name = cursor.getString(1)
                        currentAddTaskPhoneNumber = number
                        android.widget.Toast.makeText(this, "Selected: $name ($number)", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun performBackup() {
        val account = com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount(this)
        if (account == null) {
            android.widget.Toast.makeText(this, "Please sign in first", android.widget.Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            val tasks = taskManager.tasksForDay(selectedDateMillis)
            val expenses = expenseManager.expensesForDay(selectedDateMillis)
            val data = mapOf("tasks" to tasks, "expenses" to expenses)
            val json = gson.toJson(data)
            DriveBackupManager.backupData(this@MainActivity, json, account)
        }
    }

    private fun performRestore() {
        val account = com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount(this)
        if (account == null) {
            android.widget.Toast.makeText(this, "Please sign in first", android.widget.Toast.LENGTH_SHORT).show()
            return
        }
        DriveBackupManager.restoreData(this, account) { data ->
            android.widget.Toast.makeText(this, "Restored: $data", android.widget.Toast.LENGTH_SHORT).show()
            loadDataFor(selectedDateMillis)
        }
    }

    private fun showAddDialog() {
        val options = arrayOf("Task", "Expense", "Investment")
        AlertDialog.Builder(this)
            .setTitle("Add item")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showAddTaskDialog()
                    1 -> showAddExpenseDialog(false)
                    2 -> showAddExpenseDialog(true)
                }
            }
            .show()
    }

    private fun showAddTaskDialog(existingTask: TaskManager.Task? = null) {
        currentAddTaskPhoneNumber = null
        var startMillis = existingTask?.startTimeMillis ?: 0L
        var endMillis = existingTask?.endTimeMillis ?: 0L

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
            val edit = EditText(context).apply { 
                hint = "Task title"
                setText(existingTask?.title ?: "")
                setTextColor(android.graphics.Color.WHITE)
                setHintTextColor(android.graphics.Color.GRAY)
            }
            
            val timeLayout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                weightSum = 2f
            }
            val startBtn = Button(context).apply {
                text = if (startMillis > 0) SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(startMillis)) else "Start Time"
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                setOnClickListener {
                    val cal = Calendar.getInstance()
                    if (startMillis > 0) cal.timeInMillis = startMillis
                    val tpd = android.app.TimePickerDialog(context, { _, h, m ->
                        val c = Calendar.getInstance().apply {
                            timeInMillis = selectedDateMillis
                            set(Calendar.HOUR_OF_DAY, h)
                            set(Calendar.MINUTE, m)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        startMillis = c.timeInMillis
                        text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(c.time)
                    }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false)
                    tpd.show()
                }
            }
            val endBtn = Button(context).apply {
                text = if (endMillis > 0) SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(endMillis)) else "End Time"
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                setOnClickListener {
                    val cal = Calendar.getInstance()
                    if (endMillis > 0) cal.timeInMillis = endMillis
                    val tpd = android.app.TimePickerDialog(context, { _, h, m ->
                        val c = Calendar.getInstance().apply {
                            timeInMillis = selectedDateMillis
                            set(Calendar.HOUR_OF_DAY, h)
                            set(Calendar.MINUTE, m)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        endMillis = c.timeInMillis
                        text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(c.time)
                    }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false)
                    tpd.show()
                }
            }
            timeLayout.addView(startBtn)
            timeLayout.addView(endBtn)

            val spinner = Spinner(context)
            val types = TaskManager.ReminderType.values().map { it.name.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }
            spinner.adapter = android.widget.ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, types)
            existingTask?.let { spinner.setSelection(it.type.ordinal) }
            
            val phoneLayout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                visibility = if (existingTask?.type == TaskManager.ReminderType.CALL) View.VISIBLE else View.GONE
            }
            val phoneEdit = EditText(context).apply { 
                hint = "Phone number"
                inputType = InputType.TYPE_CLASS_PHONE
                setText(existingTask?.phoneNumber ?: "")
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                setTextColor(android.graphics.Color.WHITE)
                setHintTextColor(android.graphics.Color.GRAY)
            }
            val contactBtn = Button(context).apply { 
                text = "Contact" 
                setOnClickListener {
                    val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
                    startActivityForResult(intent, RC_PICK_CONTACT)
                }
            }
            phoneLayout.addView(phoneEdit)
            phoneLayout.addView(contactBtn)

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (TaskManager.ReminderType.values()[position] == TaskManager.ReminderType.CALL) {
                        phoneLayout.visibility = View.VISIBLE
                    } else {
                        phoneLayout.visibility = View.GONE
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            addView(edit)
            addView(timeLayout)
            addView(spinner)
            addView(phoneLayout)
        }
        AlertDialog.Builder(this)
            .setTitle(if (existingTask == null) "New Task" else "Edit Task")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val edit = layout.getChildAt(0) as EditText
                val spinner = layout.getChildAt(2) as Spinner
                val title = edit.text.toString()
                val day = selectedDateMillis
                val typeIndex = spinner.selectedItemPosition
                val type = TaskManager.ReminderType.values()[typeIndex]
                
                var phone: String? = null
                if (type == TaskManager.ReminderType.CALL) {
                    val phoneLayout = layout.getChildAt(3) as LinearLayout
                    phone = (phoneLayout.getChildAt(0) as EditText).text.toString()
                    if (phone.isBlank()) phone = currentAddTaskPhoneNumber
                }

                lifecycleScope.launch {
                    val taskId: Long
                    if (existingTask == null) {
                        taskId = taskManager.addTask(title, day, type, phoneNumber = phone, startTimeMillis = startMillis, endTimeMillis = endMillis)
                    } else {
                        taskId = existingTask.id
                        taskManager.updateTask(existingTask.copy(title = title, type = type, phoneNumber = phone, startTimeMillis = startMillis, endTimeMillis = endMillis))
                    }
                    
                    if (startMillis > 0) {
                        ReminderScheduler.scheduleTaskReminder(this@MainActivity, taskId, title, startMillis)
                    }

                    loadDataFor(day)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showAddExpenseDialog(isInvestment: Boolean, existingRecord: ExpenseManager.Record? = null) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
            val amountView = EditText(context).apply { 
                hint = "Amount"
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                setText(existingRecord?.amount?.toString() ?: "")
                setTextColor(android.graphics.Color.WHITE)
                setHintTextColor(android.graphics.Color.GRAY)
            }
            val descView = EditText(context).apply { 
                hint = "Description"
                setText(existingRecord?.description ?: "")
                setTextColor(android.graphics.Color.WHITE)
                setHintTextColor(android.graphics.Color.GRAY)
            }
            addView(amountView)
            addView(descView)
        }
        AlertDialog.Builder(this)
            .setTitle(if (existingRecord == null) (if (isInvestment) "New Investment" else "New Expense") else "Edit Record")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val amount = (layout.getChildAt(0) as EditText).text.toString().toDoubleOrNull() ?: 0.0
                val desc = (layout.getChildAt(1) as EditText).text.toString()
                val day = selectedDateMillis
                lifecycleScope.launch {
                    if (existingRecord == null) {
                        if (isInvestment) {
                            expenseManager.logInvestment(amount, desc, day)
                        } else {
                            expenseManager.logExpense(amount, desc, day)
                        }
                    } else {
                        val db = com.example.shreetaskmanager.database.AppDatabase.getInstance(this@MainActivity)
                        db.expenseDao().update(com.example.shreetaskmanager.database.ExpenseEntity(
                            id = existingRecord.id,
                            amount = amount,
                            description = desc,
                            dateMillis = existingRecord.dateMillis,
                            isInvestment = existingRecord.isInvestment
                        ))
                    }
                    loadDataFor(day)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menu.add(0, 1, 0, "Backup to Drive")
        menu.add(0, 2, 1, "Restore from Drive")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            1 -> performBackup()
            2 -> performRestore()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "Reminders"
            val desc = "Channel for task reminders"
            val importance = android.app.NotificationManager.IMPORTANCE_HIGH
            val channel = android.app.NotificationChannel("default", name, importance).apply { description = desc }
            val nm = getSystemService(android.app.NotificationManager::class.java)
            nm?.createNotificationChannel(channel)
        }
    }
}
