package com.example.shreetaskmanager.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
    indices = [Index(value = ["amount", "dateMillis", "source", "transactionType", "description"], unique = true)]
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val description: String?,
    val dateMillis: Long,
    val isInvestment: Boolean = false,
    val category: String = "Other",
    val source: String = "Manual", // e.g., HDFC, SBI, Cash
    val transactionType: String = "DEBIT" // DEBIT or CREDIT
)

@Entity(
    tableName = "bank_balances",
    indices = [Index(value = ["bankName", "balance", "dateMillis"], unique = true)]
)
data class BankBalanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bankName: String,
    val balance: Double,
    val dateMillis: Long,
    val lastUpdated: Long
)
