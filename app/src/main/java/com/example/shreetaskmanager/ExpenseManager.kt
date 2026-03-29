package com.example.shreetaskmanager

/**
 * Basic expense tracker for daily spending and investments.
 */
class ExpenseManager(private val dao: com.example.shreetaskmanager.database.ExpenseDao) {
    data class Record(
        val id: Long,
        val amount: Double,
        val description: String?,
        val isInvestment: Boolean,
        val dateMillis: Long,
        val category: String = "Other"
    )

    companion object {
        val EXPENSE_CATEGORIES = listOf("Grocery", "Car", "Subscriptions", "Gym", "Health", "Food", "Entertainment", "Other")
        val INVESTMENT_CATEGORIES = listOf("SIP", "Mutual Fund", "Stock Market", "FD", "Gold", "Real Estate", "Other")
    }

    suspend fun logExpense(amount: Double, description: String? = null, dateMillis: Long, category: String = "Other"): Long {
        val entity = com.example.shreetaskmanager.database.ExpenseEntity(
            amount = amount,
            description = description,
            dateMillis = dateMillis,
            isInvestment = false,
            category = category
        )
        return dao.insert(entity)
    }

    suspend fun logInvestment(amount: Double, description: String? = null, dateMillis: Long, category: String = "Other"): Long {
        val entity = com.example.shreetaskmanager.database.ExpenseEntity(
            amount = amount,
            description = description,
            dateMillis = dateMillis,
            isInvestment = true,
            category = category
        )
        return dao.insert(entity)
    }

    suspend fun totalSpent(): Double = dao.totalSpent() ?: 0.0
    suspend fun totalInvested(): Double = dao.totalInvested() ?: 0.0

    suspend fun expensesForDay(dayMillis: Long): List<Record> {
        val startOfDay = dayMillis
        val endOfDay = dayMillis + 86400000L - 1L
        return dao.expensesForDayRange(startOfDay, endOfDay).map {
            Record(it.id, it.amount, it.description, it.isInvestment, it.dateMillis, it.category)
        }
    }
}
