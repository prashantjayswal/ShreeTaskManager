package com.example.shreetaskmanager.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val dateMillis: Long,
    val dueMillis: Long? = null,
    val completed: Boolean = false,
    val type: String = "GENERIC",
    val notes: String? = null,
    val phoneNumber: String? = null,
    val estimatedTimeMinutes: Int = 0,
    val status: String = "PENDING",
    val startTimeMillis: Long = 0,
    val endTimeMillis: Long = 0
)
