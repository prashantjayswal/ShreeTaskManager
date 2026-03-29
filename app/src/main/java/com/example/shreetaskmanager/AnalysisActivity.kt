package com.example.shreetaskmanager

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.shreetaskmanager.database.AppDatabase
import com.example.shreetaskmanager.database.ExpenseEntity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import java.util.*

class AnalysisActivity : AppCompatActivity() {

    private lateinit var rvAnalysis: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var tvTotalSpent: TextView
    private lateinit var tvTotalInvested: TextView
    private lateinit var btnDateFilter: Button
    private lateinit var spinnerCategoryFilter: Spinner
    private lateinit var spinnerSortOrder: Spinner
    private lateinit var btnExport: ImageButton

    private var selectedStartDate: Long? = null
    private var selectedEndDate: Long? = null
    private var selectedCategory: String = "All"
    private var selectedSort: String = "Date DESC"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)

        tvTotalSpent = findViewById(R.id.tvTotalSpent)
        tvTotalInvested = findViewById(R.id.tvTotalInvested)
        rvAnalysis = findViewById(R.id.rvAnalysis)
        btnDateFilter = findViewById(R.id.btnDateFilter)
        spinnerCategoryFilter = findViewById(R.id.spinnerCategoryFilter)
        spinnerSortOrder = findViewById(R.id.spinnerSortOrder)
        btnExport = findViewById(R.id.btnExport)

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        NavigationUtils.setupBottomNavigation(this, navView, R.id.nav_analysis)

        setupFilters()
        
        expenseAdapter = ExpenseAdapter(mutableListOf()) { record ->
            showEditExpenseDialog(record)
        }
        rvAnalysis.layoutManager = LinearLayoutManager(this)
        rvAnalysis.adapter = expenseAdapter

        btnExport.setOnClickListener {
            performExport()
        }

        loadFilteredData()
    }

    private fun performExport() {
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(this@AnalysisActivity)
            val allTasks = db.taskDao().getAllTasks()
            val allExpenses = db.expenseDao().getAllExpenses()
            ExcelExportUtils.exportDataToExcel(this@AnalysisActivity, allTasks, allExpenses)
        }
    }

    private fun setupFilters() {
        btnDateFilter.setOnClickListener {
            val picker = MaterialDatePicker.Builder.dateRangePicker().setTitleText("Select Date Range").build()
            picker.show(supportFragmentManager, "range")
            picker.addOnPositiveButtonClickListener { range ->
                selectedStartDate = range.first
                selectedEndDate = range.second
                btnDateFilter.text = "Filtered"
                loadFilteredData()
            }
        }

        val categories = mutableListOf("All")
        categories.addAll(ExpenseManager.EXPENSE_CATEGORIES)
        categories.addAll(ExpenseManager.INVESTMENT_CATEGORIES)
        val catAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories.distinct())
        spinnerCategoryFilter.adapter = catAdapter
        spinnerCategoryFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedCategory = categories[p2]
                loadFilteredData()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        val sorts = listOf("Date DESC", "Date ASC", "Amount DESC", "Amount ASC")
        val sortAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sorts)
        spinnerSortOrder.adapter = sortAdapter
        spinnerSortOrder.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedSort = sorts[p2]
                loadFilteredData()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun loadFilteredData() {
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(this@AnalysisActivity)
            
            var queryStr = "SELECT * FROM expenses WHERE 1=1"
            val args = mutableListOf<Any>()

            if (selectedStartDate != null && selectedEndDate != null) {
                queryStr += " AND dateMillis >= ? AND dateMillis <= ?"
                args.add(selectedStartDate!!)
                args.add(selectedEndDate!!)
            }

            if (selectedCategory != "All") {
                queryStr += " AND category = ?"
                args.add(selectedCategory)
            }

            queryStr += when (selectedSort) {
                "Date DESC" -> " ORDER BY dateMillis DESC"
                "Date ASC" -> " ORDER BY dateMillis ASC"
                "Amount DESC" -> " ORDER BY amount DESC"
                "Amount ASC" -> " ORDER BY amount ASC"
                else -> " ORDER BY dateMillis DESC"
            }

            val query = SimpleSQLiteQuery(queryStr, args.toTypedArray())
            val filteredEntities = db.expenseDao().getExpensesFiltered(query)

            var spent = 0.0
            var invested = 0.0
            filteredEntities.forEach { 
                if (it.isInvestment) invested += it.amount else spent += it.amount
            }

            tvTotalSpent.text = "₹${spent.toInt()}"
            tvTotalInvested.text = "₹${invested.toInt()}"
            
            expenseAdapter.update(filteredEntities.map {
                ExpenseManager.Record(it.id, it.amount, it.description, it.isInvestment, it.dateMillis, it.category)
            })
        }
    }

    private fun showEditExpenseDialog(record: ExpenseManager.Record) {
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
            val amountView = android.widget.EditText(context).apply { 
                hint = "Amount"
                inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
                setText(record.amount.toString())
            }
            val descView = android.widget.EditText(context).apply { 
                hint = "Description"
                setText(record.description ?: "")
            }
            val spinner = Spinner(context)
            val categories = if (record.isInvestment) ExpenseManager.INVESTMENT_CATEGORIES else ExpenseManager.EXPENSE_CATEGORIES
            spinner.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, categories)
            spinner.setSelection(categories.indexOf(record.category).coerceAtLeast(0))
            
            addView(amountView)
            addView(descView)
            addView(spinner)
        }
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Edit Record")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val amount = (layout.getChildAt(0) as android.widget.EditText).text.toString().toDoubleOrNull() ?: 0.0
                val desc = (layout.getChildAt(1) as android.widget.EditText).text.toString()
                val category = (layout.getChildAt(2) as Spinner).selectedItem.toString()
                lifecycleScope.launch {
                    val db = AppDatabase.getInstance(this@AnalysisActivity)
                    db.expenseDao().update(ExpenseEntity(
                        id = record.id,
                        amount = amount,
                        description = desc,
                        dateMillis = record.dateMillis,
                        isInvestment = record.isInvestment,
                        category = category
                    ))
                    loadFilteredData()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
