package com.example.shreetaskmanager

/**
 * Simple manager for keeping track of daily tasks.
 */
class TaskManager(private val dao: com.example.shreetaskmanager.database.TaskDao) {
    enum class ReminderType { GENERIC, BIRTHDAY, ANNIVERSARY, BILL, MEETING, CALL, GROCERY, STUDY, WORK, PERSONAL }
    enum class TaskStatus { PENDING, COMPLETED, RESCHEDULED, IN_PIPELINE }

    data class Task(
        val id: Long,
        val title: String,
        val completed: Boolean,
        val dateMillis: Long,
        val type: ReminderType,
        val dueMillis: Long?,
        val notes: String?,
        val phoneNumber: String? = null,
        val estimatedTimeMinutes: Int = 0,
        val status: TaskStatus = TaskStatus.PENDING,
        val startTimeMillis: Long = 0,
        val endTimeMillis: Long = 0
    )

    suspend fun addTask(
        title: String,
        dateMillis: Long,
        type: ReminderType = ReminderType.GENERIC,
        dueMillis: Long? = null,
        notes: String? = null,
        phoneNumber: String? = null,
        estimatedTimeMinutes: Int = 0,
        startTimeMillis: Long = 0,
        endTimeMillis: Long = 0
    ): Long {
        val entity = com.example.shreetaskmanager.database.TaskEntity(
            title = title,
            dateMillis = dateMillis,
            dueMillis = dueMillis,
            completed = false,
            type = type.name,
            notes = notes,
            phoneNumber = phoneNumber,
            estimatedTimeMinutes = estimatedTimeMinutes,
            status = TaskStatus.PENDING.name,
            startTimeMillis = startTimeMillis,
            endTimeMillis = endTimeMillis
        )
        return dao.insert(entity)
    }

    suspend fun updateTask(task: Task) {
        val entity = com.example.shreetaskmanager.database.TaskEntity(
            id = task.id,
            title = task.title,
            dateMillis = task.dateMillis,
            dueMillis = task.dueMillis,
            completed = task.completed,
            type = task.type.name,
            notes = task.notes,
            phoneNumber = task.phoneNumber,
            estimatedTimeMinutes = task.estimatedTimeMinutes,
            status = task.status.name,
            startTimeMillis = task.startTimeMillis,
            endTimeMillis = task.endTimeMillis
        )
        dao.update(entity)
    }

    suspend fun completeTask(id: Long) {
        val existing = dao.getById(id)
        if (existing != null) {
            dao.update(existing.copy(completed = true, status = TaskStatus.COMPLETED.name))
        }
    }

    suspend fun updateStatus(id: Long, status: TaskStatus) {
        val existing = dao.getById(id)
        if (existing != null) {
            dao.update(existing.copy(status = status.name, completed = status == TaskStatus.COMPLETED))
        }
    }

    suspend fun moveTask(id: Long, newDate: Long, note: String?) {
        val existing = dao.getById(id)
        if (existing != null) {
            dao.update(existing.copy(dateMillis = newDate, notes = note ?: existing.notes, status = TaskStatus.RESCHEDULED.name))
        }
    }

    suspend fun tasksForDay(dayMillis: Long): List<Task> {
        return dao.tasksForDay(dayMillis).map {
            Task(
                it.id,
                it.title,
                it.completed,
                it.dateMillis,
                try { ReminderType.valueOf(it.type) } catch (_: Exception) { ReminderType.GENERIC },
                it.dueMillis,
                it.notes,
                it.phoneNumber,
                it.estimatedTimeMinutes,
                try { TaskStatus.valueOf(it.status) } catch (_: Exception) { TaskStatus.PENDING },
                it.startTimeMillis,
                it.endTimeMillis
            )
        }
    }

    suspend fun getAllTasks(): List<Task> {
        return dao.getAllTasks().map {
            Task(
                it.id,
                it.title,
                it.completed,
                it.dateMillis,
                try { ReminderType.valueOf(it.type) } catch (_: Exception) { ReminderType.GENERIC },
                it.dueMillis,
                it.notes,
                it.phoneNumber,
                it.estimatedTimeMinutes,
                try { TaskStatus.valueOf(it.status) } catch (_: Exception) { TaskStatus.PENDING },
                it.startTimeMillis,
                it.endTimeMillis
            )
        }
    }
}
