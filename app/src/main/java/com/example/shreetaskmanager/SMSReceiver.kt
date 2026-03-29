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

class SMSReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "SMSReceiver"

        fun processSMS(context: Context, sender: String, body: String, timestamp: Long = System.currentTimeMillis()) {
            val cleanBody = body.replace(",", "")
            
            // Amount detection
            val amountPattern = Pattern.compile("(?i)(?:rs\\.?|inr)\\s*([\\d]+\\.?\\d*)")
            val amountMatcher = amountPattern.matcher(cleanBody)
            
            if (!amountMatcher.find()) return
            val amount = amountMatcher.group(1)?.toDoubleOrNull() ?: return
            
            val isDebit = cleanBody.contains("debited", true) || cleanBody.contains("spent", true) || 
                          cleanBody.contains("paid", true) || cleanBody.contains("vpa", true) ||
                          cleanBody.contains("txn", true)
                          
            val isCredit = cleanBody.contains("credited", true) || cleanBody.contains("received", true) || 
                           cleanBody.contains("deposit", true)

            val type = when {
                isDebit -> "DEBIT"
                isCredit -> "CREDIT"
                else -> return 
            }

            val bankName = detectBank(sender, cleanBody) ?: return 

            saveTransaction(context, bankName, amount, type, body, timestamp)

            // Balance detection (Multi-entry history)
            val balancePattern = Pattern.compile("(?i)(?:bal|balance|avl bal).*?(?:rs\\.?|inr)\\s*([\\d]+\\.?\\d*)")
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
            return when {
                combined.contains("HDFC") -> "HDFC"
                combined.contains("SBI") || combined.contains("SBIN") -> "SBI"
                combined.contains("ICICI") -> "ICICI"
                combined.contains("AXIS") -> "AXIS"
                combined.contains("PNB") -> "PNB"
                combined.contains("KOTAK") -> "KOTAK"
                combined.contains("BOI") -> "BOI"
                else -> null
            }
        }

        private fun saveTransaction(context: Context, bank: String, amount: Double, type: String, body: String, timestamp: Long) {
            val category = autoCategorize(body)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getInstance(context)
                    db.expenseDao().insert(ExpenseEntity(
                        amount = amount,
                        description = "Auto SMS: $body",
                        dateMillis = timestamp,
                        isInvestment = category == "Investment",
                        category = category,
                        source = bank,
                        transactionType = type
                    ))
                } catch (e: Exception) { Log.e(TAG, "Error saving transaction", e) }
            }
        }

        private fun saveBalance(context: Context, bank: String, balance: Double, timestamp: Long) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getInstance(context)
                    // We save every unique balance point found in SMS to build history
                    db.bankBalanceDao().updateBalance(BankBalanceEntity(
                        bankName = bank,
                        balance = balance,
                        dateMillis = timestamp,
                        lastUpdated = System.currentTimeMillis()
                    ))
                } catch (e: Exception) { Log.e(TAG, "Error saving balance", e) }
            }
        }

        private fun autoCategorize(body: String): String {
            val b = body.lowercase()
            return when {
                b.contains("swiggy") || b.contains("zomato") || b.contains("dine") || b.contains("eats") -> "Food"
                b.contains("amazon") || b.contains("flipkart") || b.contains("myntra") || b.contains("mall") -> "Shopping"
                b.contains("jio") || b.contains("airtel") || b.contains("vi ") || b.contains("bill") || b.contains("recharge") -> "Bills"
                b.contains("uber") || b.contains("ola") || b.contains("petrol") || b.contains("hpcl") || b.contains("bpcl") -> "Travel"
                b.contains("mutual") || b.contains("fund") || b.contains("stock") || b.contains("sip") || b.contains("zerodha") -> "Investment"
                b.contains("med") || b.contains("hosp") || b.contains("pharm") -> "Health"
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
