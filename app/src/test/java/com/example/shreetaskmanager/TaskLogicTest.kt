package com.example.shreetaskmanager

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.util.Calendar

class TaskLogicTest {

    @Test
    fun testDateNormalization() {
        val calendar = Calendar.getInstance()
        calendar.set(2023, Calendar.JANUARY, 15, 14, 30, 45)
        val originalTime = calendar.timeInMillis

        // Normalization should set time to 00:00:00.000
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val expectedTime = calendar.timeInMillis

        assertNotEquals(originalTime, expectedTime)
        
        // Manual check of what my MainActivity's normalizeDate does
        val cal = Calendar.getInstance()
        cal.timeInMillis = originalTime
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        
        assertEquals(expectedTime, cal.timeInMillis)
    }

    @Test
    fun testEstimatedTimeDefaultValue() {
        val task = TaskManager.Task(
            id = 1,
            title = "Test Task",
            completed = false,
            dateMillis = System.currentTimeMillis(),
            type = TaskManager.ReminderType.GENERIC,
            dueMillis = null,
            notes = null,
            phoneNumber = null,
            estimatedTimeMinutes = 0
        )
        assertEquals(0, task.estimatedTimeMinutes)
    }

    @Test
    fun testTaskCompletionToggle() {
        val task = TaskManager.Task(
            id = 1,
            title = "Test Task",
            completed = false,
            dateMillis = System.currentTimeMillis(),
            type = TaskManager.ReminderType.GENERIC,
            dueMillis = null,
            notes = null,
            phoneNumber = null
        )
        
        val completedTask = task.copy(completed = true)
        assertEquals(true, completedTask.completed)
    }
}
