package com.example.shreetaskmanager.database

import androidx.room.*

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: TaskEntity): Long

    @Update
    suspend fun update(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE dateMillis = :dayMillis")
    suspend fun tasksForDay(dayMillis: Long): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: Long): TaskEntity?

    @Query("SELECT * FROM tasks WHERE dateMillis >= :start AND dateMillis <= :end")
    suspend fun getTasksInRange(start: Long, end: Long): List<TaskEntity>

    @Query("SELECT * FROM tasks ORDER BY dateMillis DESC")
    suspend fun getAllTasks(): List<TaskEntity>

    @Query("DELETE FROM tasks")
    suspend fun deleteAll()
}
