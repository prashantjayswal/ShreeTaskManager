package com.example.shreetaskmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.example.shreetaskmanager.database.AppDatabase
import com.example.shreetaskmanager.database.BankBalanceEntity
import com.example.shreetaskmanager.database.ExpenseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern

/**
 * Intelligent SMS Receiver that parses bank transactions and auto-categorizes them.
 */
class SMSReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "SMSReceiver"

        fun processSMS(context: Context, sender: String, body: String, timestamp: Long = System.currentTimeMillis()) {
            val cleanBody = body.replace(",", "")
            
            // Intelligence: Extract Amount using a more robust regex
            // Supports formats like "Rs. 100", "INR 100.50", "Rs100"
            val amountPattern = Pattern.compile("(?i)(?:rs\\.?|inr|amt)\\s*([\\d]+\\.?\\d*)")
            val amountMatcher = amountPattern.matcher(cleanBody)
            
            if (!amountMatcher.find()) return
            val amount = amountMatcher.group(1)?.toDoubleOrNull() ?: return
            
            // Intelligence: Improved Transaction Type Detection
            val isDebit = cleanBody.contains("debited", true) || 
                          cleanBody.contains("spent", true) || 
                          cleanBody.contains("paid", true) || 
                          cleanBody.contains("vpa", true) ||
                          cleanBody.contains("txn", true) ||
                          cleanBody.contains("purchase", true)
                          
            val isCredit = cleanBody.contains("credited", true) || 
                           cleanBody.contains("received", true) || 
                           cleanBody.contains("deposit", true) ||
                           cleanBody.contains("added", true)

            val type = when {
                isDebit -> "DEBIT"
                isCredit -> "CREDIT"
                else -> return // Skip non-transactional messages
            }

            // Intelligence: Broad Bank Detection
            val bankName = detectBank(sender, cleanBody) ?: "Other Bank"

            saveTransaction(context, bankName, amount, type, body, timestamp)

            // Intelligence: Point-in-time Balance Extraction
            val balancePattern = Pattern.compile("(?i)(?:bal|balance|avl bal|limit).*?(?:rs\\.?|inr)\\s*([\\d]+\\.?\\d*)")
            val balanceMatcher = balancePattern.matcher(cleanBody)
            if (balanceMatcher.find()) {
                val balance = balanceMatcher.group(1)?.toDoubleOrNull()
                if (balance != null) {
                    saveBalance(context, bankName, balance, timestamp)
                }
            }
        }

        private fun detectBank(sender: String, body: String): String? {
            val combined = (sender + body).uppercase()
            val banks = listOf("HDFC", "SBI", "ICICI", "AXIS", "PNB", "KOTAK", "BOI", "YESBNK", "HSBC", "CANARA", "IDBI", "RBL", "BOB")
            return banks.find { combined.contains(it) }
        }

        private fun saveTransaction(context: Context, bank: String, amount: Double, type: String, body: String, timestamp: Long) {
            val category = autoCategorize(body)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getInstance(context)
                    // ConflictStrategy.IGNORE handles unique constraint in version 9
                    db.expenseDao().insert(ExpenseEntity(
                        amount = amount,
                        description = "Auto SMS: $body",
                        dateMillis = timestamp,
                        isInvestment = category == "Investment",
                        category = category,
                        source = bank,
                        transactionType = type
                    ))
                } catch (e: Exception) {
                    Log.e(TAG, "Database error while saving transaction", e)
                }
            }
        }

        private fun saveBalance(context: Context, bank: String, balance: Double, timestamp: Long) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getInstance(context)
                    db.bankBalanceDao().updateBalance(BankBalanceEntity(
                        bankName = bank,
                        balance = balance,
                        dateMillis = timestamp,
                        lastUpdated = System.currentTimeMillis()
                    ))
                } catch (e: Exception) {
                    Log.e(TAG, "Database error while saving balance", e)
                }
            }
        }

        /**
         * AI-like categorization logic based on merchant and transaction keywords.
         */
        private fun autoCategorize(body: String): String {
            val b = body.lowercase()
            return when {
                b.contains("swiggy") || b.contains("zomato") || b.contains("dine") || b.contains("food") || b.contains("restaurant") -> "Food"
                b.contains("amazon") || b.contains("flipkart") || b.contains("myntra") || b.contains("mall") || b.contains("shopping") -> "Shopping"
                b.contains("jio") || b.contains("airtel") || b.contains("vi ") || b.contains("bill") || b.contains("recharge") || b.contains("utility") -> "Bills"
                b.contains("uber") || b.contains("ola") || b.contains("petrol") || b.contains("hpcl") || b.contains("fuel") || b.contains("metro") -> "Travel"
                b.contains("mutual") || b.contains("fund") || b.contains("stock") || b.contains("sip") || b.contains("zerodha") || b.contains("upstox") -> "Investment"
                b.contains("hospital") || b.contains("med") || b.contains("pharmacy") || b.contains("apollo") -> "Health"
                b.contains("netflix") || b.contains("prime") || b.contains("hotstar") || b.contains("movie") || b.contains("theater") -> "Entertainment"
                else -> "Other"
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (sms in messages) {
                processSMS(context, sms.displayOriginatingAddress ?: "Unknown", sms.displayMessageBody)
            }
        }
    }
}
