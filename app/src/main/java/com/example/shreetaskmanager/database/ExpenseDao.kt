package com.example.shreetaskmanager.database

import androidx.room.*

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(expense: ExpenseEntity): Long

    @Update
    suspend fun update(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses WHERE dateMillis >= :dayStart AND dateMillis <= :dayEnd ORDER BY dateMillis DESC")
    suspend fun expensesForDayRange(dayStart: Long, dayEnd: Long): List<ExpenseEntity>

    @Query("SELECT SUM(amount) FROM expenses WHERE isInvestment = 0 AND transactionType = 'DEBIT'")
    suspend fun totalSpent(): Double?

    @Query("SELECT SUM(amount) FROM expenses WHERE isInvestment = 1 AND transactionType = 'DEBIT'")
    suspend fun totalInvested(): Double?

    @Query("SELECT SUM(amount) FROM expenses WHERE dateMillis >= :start AND dateMillis <= :end AND isInvestment = 0 AND transactionType = 'DEBIT'")
    suspend fun totalSpentInRange(start: Long, end: Long): Double?

    @Query("SELECT SUM(amount) FROM expenses WHERE dateMillis >= :start AND dateMillis <= :end AND isInvestment = 1 AND transactionType = 'DEBIT'")
    suspend fun totalInvestedInRange(start: Long, end: Long): Double?

    @Query("SELECT * FROM expenses ORDER BY dateMillis DESC")
    suspend fun getAllExpenses(): List<ExpenseEntity>

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getById(id: Long): ExpenseEntity?

    @RawQuery
    suspend fun getExpensesFiltered(query: androidx.sqlite.db.SupportSQLiteQuery): List<ExpenseEntity>

    @Query("DELETE FROM expenses")
    suspend fun deleteAll()
}

@Dao
interface BankBalanceDao {
    @Query("SELECT * FROM bank_balances ORDER BY dateMillis DESC")
    suspend fun getAllBalances(): List<BankBalanceEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateBalance(balance: BankBalanceEntity)

    @Query("SELECT * FROM bank_balances WHERE bankName = :bankName ORDER BY dateMillis DESC")
    suspend fun getBalancesByBank(bankName: String): List<BankBalanceEntity>

    @Query("SELECT * FROM bank_balances WHERE bankName = :bankName AND dateMillis <= :targetDate ORDER BY dateMillis DESC LIMIT 1")
    suspend fun getBalanceAtDate(bankName: String, targetDate: Long): BankBalanceEntity?
    
    @Query("SELECT DISTINCT bankName FROM bank_balances")
    suspend fun getUniqueBanks(): List<String>

    @Query("DELETE FROM bank_balances")
    suspend fun deleteAll()
}
