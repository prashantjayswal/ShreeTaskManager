package com.example.shreetaskmanager

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.example.shreetaskmanager.database.ExpenseEntity
import com.example.shreetaskmanager.database.TaskEntity
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object ExcelExportUtils {

    fun exportDataToExcel(context: Context, tasks: List<TaskEntity>, expenses: List<ExpenseEntity>) {
        val workbook = XSSFWorkbook()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        // Tasks Sheet
        val taskSheet = workbook.createSheet("Tasks")
        val taskHeader = taskSheet.createRow(0)
        taskHeader.createCell(0).setCellValue("Title")
        taskHeader.createCell(1).setCellValue("Date")
        taskHeader.createCell(2).setCellValue("Start Time")
        taskHeader.createCell(3).setCellValue("End Time")
        taskHeader.createCell(4).setCellValue("Type")
        taskHeader.createCell(5).setCellValue("Status")
        taskHeader.createCell(6).setCellValue("Notes")

        tasks.forEachIndexed { index, task ->
            val row = taskSheet.createRow(index + 1)
            row.createCell(0).setCellValue(task.title)
            row.createCell(1).setCellValue(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(task.dateMillis)))
            row.createCell(2).setCellValue(if (task.startTimeMillis > 0) sdf.format(Date(task.startTimeMillis)) else "")
            row.createCell(3).setCellValue(if (task.endTimeMillis > 0) sdf.format(Date(task.endTimeMillis)) else "")
            row.createCell(4).setCellValue(task.type)
            row.createCell(5).setCellValue(task.status)
            row.createCell(6).setCellValue(task.notes ?: "")
        }

        // Expenses Sheet
        val expenseSheet = workbook.createSheet("Expenses & Investments")
        val expenseHeader = expenseSheet.createRow(0)
        expenseHeader.createCell(0).setCellValue("Type")
        expenseHeader.createCell(1).setCellValue("Category")
        expenseHeader.createCell(2).setCellValue("Amount")
        expenseHeader.createCell(3).setCellValue("Description")
        expenseHeader.createCell(4).setCellValue("Date")

        expenses.forEachIndexed { index, expense ->
            val row = expenseSheet.createRow(index + 1)
            row.createCell(0).setCellValue(if (expense.isInvestment) "Investment" else "Expense")
            row.createCell(1).setCellValue(expense.category)
            row.createCell(2).setCellValue(expense.amount)
            row.createCell(3).setCellValue(expense.description ?: "")
            row.createCell(4).setCellValue(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(expense.dateMillis)))
        }

        try {
            val fileName = "ShreeTaskManager_Export_${System.currentTimeMillis()}.xlsx"
            val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            val fileOut = FileOutputStream(filePath)
            workbook.write(fileOut)
            fileOut.close()
            workbook.close()
            Toast.makeText(context, "Exported to: ${filePath.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Export failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
