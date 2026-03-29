package com.example.shreetaskmanager

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shreetaskmanager.database.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var taskManager: TaskManager
    private lateinit var expenseManager: ExpenseManager
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var tvTotalTasks: TextView
    private lateinit var tvMonthExpense: TextView
    private lateinit var tvMonthInvestment: TextView
    private lateinit var tvConnectedEmail: TextView
    private lateinit var tvTasksTodayRange: TextView
    private lateinit var tvSpentRange: TextView
    private lateinit var tvInvestedRange: TextView
    private lateinit var btnLinkEmail: View
    private lateinit var btnSyncInbox: View
    private lateinit var btnBackup: View
    private lateinit var btnLocalBackup: View
    private lateinit var btnRestore: View
    private lateinit var btnCleanAll: View
    private lateinit var rvBankBalances: RecyclerView
    private lateinit var layoutBankBalances: View

    private val RC_SMS_PERMISSION = 1001
    private val RC_SIGN_IN = 1002

    private val createBackupFile = registerForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        uri?.let { saveBackupToUri(it) }
    }

    private val openBackupFile = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let { restoreBackupFromUri(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val db = AppDatabase.getInstance(this)
        taskManager = TaskManager(db.taskDao())
        expenseManager = ExpenseManager(db.expenseDao())

        tvTotalTasks = findViewById(R.id.tvTotalTasks)
        tvMonthExpense = findViewById(R.id.tvMonthExpense)
        tvMonthInvestment = findViewById(R.id.tvMonthInvestment)
        tvConnectedEmail = findViewById(R.id.tvConnectedEmail)
        tvTasksTodayRange = findViewById(R.id.tvTasksTodayRange)
        tvSpentRange = findViewById(R.id.tvSpentRange)
        tvInvestedRange = findViewById(R.id.tvInvestedRange)
        btnLinkEmail = findViewById(R.id.btnLinkEmail)
        btnSyncInbox = findViewById(R.id.btnSyncInbox)
        btnBackup = findViewById(R.id.btnBackup)
        btnLocalBackup = findViewById(R.id.btnLocalBackup)
        btnRestore = findViewById(R.id.btnRestore)
        btnCleanAll = findViewById(R.id.btnCleanAll)
        rvBankBalances = findViewById(R.id.rvBankBalances)
        layoutBankBalances = findViewById(R.id.layoutBankBalances)

        rvBankBalances.layoutManager = LinearLayoutManager(this)

        setupGoogleSignIn()
        checkPermissions()
        setupClickListeners()
        updateUserUI()
        loadSummary()

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        NavigationUtils.setupBottomNavigation(this, navView, R.id.nav_home)
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope("https://www.googleapis.com/auth/gmail.readonly"), Scope("https://www.googleapis.com/auth/drive.file"))
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun updateUserUI() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            tvConnectedEmail.text = "Connected: ${account.email}"
            tvConnectedEmail.visibility = View.VISIBLE
            btnLinkEmail.visibility = View.GONE
        } else {
            tvConnectedEmail.visibility = View.GONE
            btnLinkEmail.visibility = View.VISIBLE
        }
    }

    private fun checkPermissions() {
        val permissions = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.RECEIVE_SMS)
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.READ_SMS)
        }
        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), RC_SMS_PERMISSION)
        }
    }

    private fun setupClickListeners() {
        btnLinkEmail.setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
        }
        
        btnSyncInbox.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                syncAllPastSMS()
            } else {
                Toast.makeText(this, "Permission required to sync SMS", Toast.LENGTH_SHORT).show()
                checkPermissions()
            }
        }

        btnBackup.setOnClickListener {
            performCloudBackup()
        }

        btnLocalBackup.setOnClickListener {
            val fileName = "shree_backup_${SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())}.json"
            createBackupFile.launch(fileName)
        }

        btnRestore.setOnClickListener {
            showRestoreOptions()
        }

        btnCleanAll.setOnClickListener {
            showCleanConfirmation()
        }

        findViewById<View>(R.id.cardTasksToday).setOnClickListener {
            startActivity(Intent(this, AllTasksActivity::class.java))
        }
        findViewById<View>(R.id.cardExpense).setOnClickListener {
            startActivity(Intent(this, AnalysisActivity::class.java))
        }
        findViewById<View>(R.id.cardInvestment).setOnClickListener {
            startActivity(Intent(this, AnalysisActivity::class.java))
        }
        findViewById<View>(R.id.btnCalendar).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun performCloudBackup() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account == null) {
            Toast.makeText(this, "Please link email first", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            val json = getFullBackupJson()
            DriveBackupManager.backupData(this@HomeActivity, json, account)
            Toast.makeText(this@HomeActivity, "Backup initiated to Drive", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun getFullBackupJson(): String = withContext(Dispatchers.IO) {
        val db = AppDatabase.getInstance(this@HomeActivity)
        val data = mapOf(
            "tasks" to db.taskDao().getAllTasks(),
            "expenses" to db.expenseDao().getAllExpenses(),
            "balances" to db.bankBalanceDao().getAllBalances()
        )
        Gson().toJson(data)
    }

    private fun saveBackupToUri(uri: Uri) {
        lifecycleScope.launch {
            val json = getFullBackupJson()
            withContext(Dispatchers.IO) {
                contentResolver.openOutputStream(uri)?.use { 
                    it.write(json.toByteArray())
                }
            }
            Toast.makeText(this@HomeActivity, "Local backup saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showRestoreOptions() {
        val options = arrayOf("Restore from Cloud (Drive)", "Restore from Local File")
        AlertDialog.Builder(this)
            .setTitle("Restore Data")
            .setItems(options) { _, which ->
                if (which == 0) performCloudRestore()
                else openBackupFile.launch(arrayOf("application/json"))
            }
            .show()
    }

    private fun performCloudRestore() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account == null) {
            Toast.makeText(this, "Please link email first", Toast.LENGTH_SHORT).show()
            return
        }
        DriveBackupManager.restoreData(this, account) { json ->
            processRestoreJson(json)
        }
    }

    private fun restoreBackupFromUri(uri: Uri) {
        lifecycleScope.launch {
            val json = withContext(Dispatchers.IO) {
                contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() } ?: ""
            }
            processRestoreJson(json)
        }
    }

    private fun processRestoreJson(json: String) {
        if (json.isBlank()) return
        lifecycleScope.launch {
            try {
                val db = AppDatabase.getInstance(this@HomeActivity)
                val type = object : TypeToken<Map<String, List<Any>>>() {}.type
                val data: Map<String, List<Any>> = Gson().fromJson(json, type)
                
                withContext(Dispatchers.IO) {
                    // Restore Tasks
                    val taskType = object : TypeToken<List<TaskEntity>>() {}.type
                    val tasks: List<TaskEntity> = Gson().fromJson(Gson().toJson(data["tasks"]), taskType)
                    tasks.forEach { db.taskDao().insert(it) }

                    // Restore Expenses
                    val expenseType = object : TypeToken<List<ExpenseEntity>>() {}.type
                    val expenses: List<ExpenseEntity> = Gson().fromJson(Gson().toJson(data["expenses"]), expenseType)
                    expenses.forEach { db.expenseDao().insert(it) }

                    // Restore Balances
                    val balanceType = object : TypeToken<List<BankBalanceEntity>>() {}.type
                    val balances: List<BankBalanceEntity> = Gson().fromJson(Gson().toJson(data["balances"]), balanceType)
                    balances.forEach { db.bankBalanceDao().updateBalance(it) }
                }
                Toast.makeText(this@HomeActivity, "Restore Complete!", Toast.LENGTH_SHORT).show()
                loadSummary()
            } catch (e: Exception) {
                Toast.makeText(this@HomeActivity, "Restore Failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showCleanConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Clean All Data")
            .setMessage("Are you sure? This cannot be undone.")
            .setPositiveButton("Clean") { _, _ ->
                lifecycleScope.launch {
                    val db = AppDatabase.getInstance(this@HomeActivity)
                    withContext(Dispatchers.IO) {
                        db.taskDao().deleteAll()
                        db.expenseDao().deleteAll()
                        db.bankBalanceDao().deleteAll()
                    }
                    Toast.makeText(this@HomeActivity, "Data cleaned", Toast.LENGTH_SHORT).show()
                    loadSummary()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun syncAllPastSMS() {
        lifecycleScope.launch {
            Toast.makeText(this@HomeActivity, "Syncing past transactions...", Toast.LENGTH_SHORT).show()
            withContext(Dispatchers.IO) {
                val cursor = contentResolver.query(
                    android.net.Uri.parse("content://sms/inbox"),
                    arrayOf("address", "body", "date"),
                    null, null, "date DESC"
                )
                cursor?.use {
                    val addressIdx = it.getColumnIndex("address")
                    val bodyIdx = it.getColumnIndex("body")
                    val dateIdx = it.getColumnIndex("date")
                    while (it.moveToNext()) {
                        val sender = it.getString(addressIdx)
                        val body = it.getString(bodyIdx)
                        val date = it.getLong(dateIdx)
                        SMSReceiver.processSMS(this@HomeActivity, sender, body, date)
                    }
                }
            }
            Toast.makeText(this@HomeActivity, "Sync Complete!", Toast.LENGTH_SHORT).show()
            loadSummary()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            updateUserUI()
        }
    }

    override fun onResume() {
        super.onResume()
        loadSummary()
    }

    private fun loadSummary() {
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(this@HomeActivity)
            val sdfFull = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
            val sdfShort = SimpleDateFormat("d MMM", Locale.getDefault())
            
            // Tasks Range
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
            }
            tvTasksTodayRange.text = "Tasks for ${sdfFull.format(today.time)}"
            tvTotalTasks.text = taskManager.tasksForDay(today.timeInMillis).size.toString()

            // Financials Range
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val startOfMonth = calendar.timeInMillis
            val startText = sdfShort.format(Date(startOfMonth))
            
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            val endOfMonth = calendar.timeInMillis
            val endText = sdfShort.format(Date(endOfMonth))
            
            tvSpentRange.text = "Spent ($startText - $endText)"
            tvInvestedRange.text = "Invested ($startText - $endText)"

            val monthExpense = db.expenseDao().totalSpentInRange(startOfMonth, endOfMonth) ?: 0.0
            val monthInvestment = db.expenseDao().totalInvestedInRange(startOfMonth, endOfMonth) ?: 0.0
            tvMonthExpense.text = "₹${monthExpense.toInt()}"
            tvMonthInvestment.text = "₹${monthInvestment.toInt()}"

            // Unique Bank Balances (Latest for each bank)
            val allBalances = db.bankBalanceDao().getAllBalances()
            val latestPerBank = allBalances.distinctBy { it.bankName }
            
            if (latestPerBank.isNotEmpty()) {
                layoutBankBalances.visibility = View.VISIBLE
                rvBankBalances.adapter = BankBalanceAdapter(latestPerBank)
            } else {
                layoutBankBalances.visibility = View.GONE
            }
        }
    }

    class BankBalanceAdapter(private val balances: List<BankBalanceEntity>) : RecyclerView.Adapter<BankBalanceAdapter.ViewHolder>() {
        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val bankName: TextView = v.findViewById(android.R.id.text1)
            val balance: TextView = v.findViewById(android.R.id.text2)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
            return ViewHolder(v)
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = balances[position]
            val sdf = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
            holder.bankName.text = "${item.bankName} - Balance on ${sdf.format(Date(item.dateMillis))}"
            holder.bankName.setTextColor(android.graphics.Color.WHITE)
            holder.balance.text = "₹${item.balance.toInt()}"
            holder.balance.setTextColor(android.graphics.Color.LTGRAY)
        }
        override fun getItemCount() = balances.size
    }
}
