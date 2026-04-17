package com.example.shreetaskmanager

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.regex.Pattern

class SMSParsingTest {

    @Test
    fun testDebitAmountExtraction() {
        val body = "Your A/c XXX1234 is debited by Rs. 500.50 on 15-03-26. Info: Swiggy. Avl Bal: Rs. 1000.00"
        val amountPattern = Pattern.compile("(?i)(?:rs\\.?|inr|amt)\\s*([\\d]+\\.?\\d*)")
        val matcher = amountPattern.matcher(body.replace(",", ""))
        
        assert(matcher.find())
        val amount = matcher.group(1)?.toDoubleOrNull()
        assertEquals(500.5, amount!!, 0.01)
    }

    @Test
    fun testBalanceExtraction() {
        val body = "Your A/c XXX1234 is debited by Rs. 500.50. Avl Bal: Rs. 1,234.56"
        val balancePattern = Pattern.compile("(?i)(?:bal|balance|avl bal|limit).*?(?:rs\\.?|inr)\\s*([\\d]+\\.?\\d*)")
        val matcher = balancePattern.matcher(body.replace(",", ""))
        
        assert(matcher.find())
        val balance = matcher.group(1)?.toDoubleOrNull()
        assertEquals(1234.56, balance!!, 0.01)
    }

    @Test
    fun testBankDetection() {
        val sender = "AD-HDFCBK"
        val body = "HDFC Bank: Rs 100 spent on card."
        val combined = (sender + body).uppercase()
        val banks = listOf("HDFC", "SBI", "ICICI", "AXIS")
        val detected = banks.find { combined.contains(it) }
        assertEquals("HDFC", detected)
    }

    @Test
    fun testAutoCategorization() {
        val bodies = mapOf(
            "Swiggy order" to "Food",
            "Amazon shopping" to "Shopping",
            "Jio Recharge" to "Bills",
            "Uber trip" to "Travel",
            "Mutual Fund SIP" to "Investment"
        )

        bodies.forEach { (body, expectedCategory) ->
            val category = autoCategorizeMock(body)
            assertEquals(expectedCategory, category)
        }
    }

    private fun autoCategorizeMock(body: String): String {
        val b = body.lowercase()
        return when {
            b.contains("swiggy") || b.contains("zomato") -> "Food"
            b.contains("amazon") || b.contains("flipkart") -> "Shopping"
            b.contains("jio") || b.contains("airtel") || b.contains("bill") -> "Bills"
            b.contains("uber") || b.contains("ola") -> "Travel"
            b.contains("mutual") || b.contains("sip") -> "Investment"
            else -> "Other"
        }
    }
}
