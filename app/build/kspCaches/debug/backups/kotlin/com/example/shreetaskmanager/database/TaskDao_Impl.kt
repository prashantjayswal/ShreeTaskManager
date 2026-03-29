package com.example.shreetaskmanager.database

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class TaskDao_Impl(
  __db: RoomDatabase,
) : TaskDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTaskEntity: EntityInsertAdapter<TaskEntity>

  private val __updateAdapterOfTaskEntity: EntityDeleteOrUpdateAdapter<TaskEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfTaskEntity = object : EntityInsertAdapter<TaskEntity>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `tasks` (`id`,`title`,`dateMillis`,`dueMillis`,`completed`,`type`,`notes`,`phoneNumber`,`estimatedTimeMinutes`,`status`,`startTimeMillis`,`endTimeMillis`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TaskEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindLong(3, entity.dateMillis)
        val _tmpDueMillis: Long? = entity.dueMillis
        if (_tmpDueMillis == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmpDueMillis)
        }
        val _tmp: Int = if (entity.completed) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        statement.bindText(6, entity.type)
        val _tmpNotes: String? = entity.notes
        if (_tmpNotes == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpNotes)
        }
        val _tmpPhoneNumber: String? = entity.phoneNumber
        if (_tmpPhoneNumber == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpPhoneNumber)
        }
        statement.bindLong(9, entity.estimatedTimeMinutes.toLong())
        statement.bindText(10, entity.status)
        statement.bindLong(11, entity.startTimeMillis)
        statement.bindLong(12, entity.endTimeMillis)
      }
    }
    this.__updateAdapterOfTaskEntity = object : EntityDeleteOrUpdateAdapter<TaskEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `tasks` SET `id` = ?,`title` = ?,`dateMillis` = ?,`dueMillis` = ?,`completed` = ?,`type` = ?,`notes` = ?,`phoneNumber` = ?,`estimatedTimeMinutes` = ?,`status` = ?,`startTimeMillis` = ?,`endTimeMillis` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: TaskEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindLong(3, entity.dateMillis)
        val _tmpDueMillis: Long? = entity.dueMillis
        if (_tmpDueMillis == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmpDueMillis)
        }
        val _tmp: Int = if (entity.completed) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        statement.bindText(6, entity.type)
        val _tmpNotes: String? = entity.notes
        if (_tmpNotes == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpNotes)
        }
        val _tmpPhoneNumber: String? = entity.phoneNumber
        if (_tmpPhoneNumber == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpPhoneNumber)
        }
        statement.bindLong(9, entity.estimatedTimeMinutes.toLong())
        statement.bindText(10, entity.status)
        statement.bindLong(11, entity.startTimeMillis)
        statement.bindLong(12, entity.endTimeMillis)
        statement.bindLong(13, entity.id)
      }
    }
  }

  public override suspend fun insert(task: TaskEntity): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfTaskEntity.insertAndReturnId(_connection, task)
    _result
  }

  public override suspend fun update(task: TaskEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfTaskEntity.handle(_connection, task)
  }

  public override suspend fun tasksForDay(dayMillis: Long): List<TaskEntity> {
    val _sql: String = "SELECT * FROM tasks WHERE dateMillis = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, dayMillis)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDateMillis: Int = getColumnIndexOrThrow(_stmt, "dateMillis")
        val _columnIndexOfDueMillis: Int = getColumnIndexOrThrow(_stmt, "dueMillis")
        val _columnIndexOfCompleted: Int = getColumnIndexOrThrow(_stmt, "completed")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfPhoneNumber: Int = getColumnIndexOrThrow(_stmt, "phoneNumber")
        val _columnIndexOfEstimatedTimeMinutes: Int = getColumnIndexOrThrow(_stmt, "estimatedTimeMinutes")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfStartTimeMillis: Int = getColumnIndexOrThrow(_stmt, "startTimeMillis")
        val _columnIndexOfEndTimeMillis: Int = getColumnIndexOrThrow(_stmt, "endTimeMillis")
        val _result: MutableList<TaskEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TaskEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDateMillis: Long
          _tmpDateMillis = _stmt.getLong(_columnIndexOfDateMillis)
          val _tmpDueMillis: Long?
          if (_stmt.isNull(_columnIndexOfDueMillis)) {
            _tmpDueMillis = null
          } else {
            _tmpDueMillis = _stmt.getLong(_columnIndexOfDueMillis)
          }
          val _tmpCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfCompleted).toInt()
          _tmpCompleted = _tmp != 0
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpPhoneNumber: String?
          if (_stmt.isNull(_columnIndexOfPhoneNumber)) {
            _tmpPhoneNumber = null
          } else {
            _tmpPhoneNumber = _stmt.getText(_columnIndexOfPhoneNumber)
          }
          val _tmpEstimatedTimeMinutes: Int
          _tmpEstimatedTimeMinutes = _stmt.getLong(_columnIndexOfEstimatedTimeMinutes).toInt()
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpStartTimeMillis: Long
          _tmpStartTimeMillis = _stmt.getLong(_columnIndexOfStartTimeMillis)
          val _tmpEndTimeMillis: Long
          _tmpEndTimeMillis = _stmt.getLong(_columnIndexOfEndTimeMillis)
          _item = TaskEntity(_tmpId,_tmpTitle,_tmpDateMillis,_tmpDueMillis,_tmpCompleted,_tmpType,_tmpNotes,_tmpPhoneNumber,_tmpEstimatedTimeMinutes,_tmpStatus,_tmpStartTimeMillis,_tmpEndTimeMillis)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(id: Long): TaskEntity? {
    val _sql: String = "SELECT * FROM tasks WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDateMillis: Int = getColumnIndexOrThrow(_stmt, "dateMillis")
        val _columnIndexOfDueMillis: Int = getColumnIndexOrThrow(_stmt, "dueMillis")
        val _columnIndexOfCompleted: Int = getColumnIndexOrThrow(_stmt, "completed")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfPhoneNumber: Int = getColumnIndexOrThrow(_stmt, "phoneNumber")
        val _columnIndexOfEstimatedTimeMinutes: Int = getColumnIndexOrThrow(_stmt, "estimatedTimeMinutes")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfStartTimeMillis: Int = getColumnIndexOrThrow(_stmt, "startTimeMillis")
        val _columnIndexOfEndTimeMillis: Int = getColumnIndexOrThrow(_stmt, "endTimeMillis")
        val _result: TaskEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDateMillis: Long
          _tmpDateMillis = _stmt.getLong(_columnIndexOfDateMillis)
          val _tmpDueMillis: Long?
          if (_stmt.isNull(_columnIndexOfDueMillis)) {
            _tmpDueMillis = null
          } else {
            _tmpDueMillis = _stmt.getLong(_columnIndexOfDueMillis)
          }
          val _tmpCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfCompleted).toInt()
          _tmpCompleted = _tmp != 0
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpPhoneNumber: String?
          if (_stmt.isNull(_columnIndexOfPhoneNumber)) {
            _tmpPhoneNumber = null
          } else {
            _tmpPhoneNumber = _stmt.getText(_columnIndexOfPhoneNumber)
          }
          val _tmpEstimatedTimeMinutes: Int
          _tmpEstimatedTimeMinutes = _stmt.getLong(_columnIndexOfEstimatedTimeMinutes).toInt()
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpStartTimeMillis: Long
          _tmpStartTimeMillis = _stmt.getLong(_columnIndexOfStartTimeMillis)
          val _tmpEndTimeMillis: Long
          _tmpEndTimeMillis = _stmt.getLong(_columnIndexOfEndTimeMillis)
          _result = TaskEntity(_tmpId,_tmpTitle,_tmpDateMillis,_tmpDueMillis,_tmpCompleted,_tmpType,_tmpNotes,_tmpPhoneNumber,_tmpEstimatedTimeMinutes,_tmpStatus,_tmpStartTimeMillis,_tmpEndTimeMillis)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTasksInRange(start: Long, end: Long): List<TaskEntity> {
    val _sql: String = "SELECT * FROM tasks WHERE dateMillis >= ? AND dateMillis <= ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, start)
        _argIndex = 2
        _stmt.bindLong(_argIndex, end)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDateMillis: Int = getColumnIndexOrThrow(_stmt, "dateMillis")
        val _columnIndexOfDueMillis: Int = getColumnIndexOrThrow(_stmt, "dueMillis")
        val _columnIndexOfCompleted: Int = getColumnIndexOrThrow(_stmt, "completed")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfPhoneNumber: Int = getColumnIndexOrThrow(_stmt, "phoneNumber")
        val _columnIndexOfEstimatedTimeMinutes: Int = getColumnIndexOrThrow(_stmt, "estimatedTimeMinutes")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfStartTimeMillis: Int = getColumnIndexOrThrow(_stmt, "startTimeMillis")
        val _columnIndexOfEndTimeMillis: Int = getColumnIndexOrThrow(_stmt, "endTimeMillis")
        val _result: MutableList<TaskEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TaskEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDateMillis: Long
          _tmpDateMillis = _stmt.getLong(_columnIndexOfDateMillis)
          val _tmpDueMillis: Long?
          if (_stmt.isNull(_columnIndexOfDueMillis)) {
            _tmpDueMillis = null
          } else {
            _tmpDueMillis = _stmt.getLong(_columnIndexOfDueMillis)
          }
          val _tmpCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfCompleted).toInt()
          _tmpCompleted = _tmp != 0
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpPhoneNumber: String?
          if (_stmt.isNull(_columnIndexOfPhoneNumber)) {
            _tmpPhoneNumber = null
          } else {
            _tmpPhoneNumber = _stmt.getText(_columnIndexOfPhoneNumber)
          }
          val _tmpEstimatedTimeMinutes: Int
          _tmpEstimatedTimeMinutes = _stmt.getLong(_columnIndexOfEstimatedTimeMinutes).toInt()
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpStartTimeMillis: Long
          _tmpStartTimeMillis = _stmt.getLong(_columnIndexOfStartTimeMillis)
          val _tmpEndTimeMillis: Long
          _tmpEndTimeMillis = _stmt.getLong(_columnIndexOfEndTimeMillis)
          _item = TaskEntity(_tmpId,_tmpTitle,_tmpDateMillis,_tmpDueMillis,_tmpCompleted,_tmpType,_tmpNotes,_tmpPhoneNumber,_tmpEstimatedTimeMinutes,_tmpStatus,_tmpStartTimeMillis,_tmpEndTimeMillis)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllTasks(): List<TaskEntity> {
    val _sql: String = "SELECT * FROM tasks ORDER BY dateMillis DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDateMillis: Int = getColumnIndexOrThrow(_stmt, "dateMillis")
        val _columnIndexOfDueMillis: Int = getColumnIndexOrThrow(_stmt, "dueMillis")
        val _columnIndexOfCompleted: Int = getColumnIndexOrThrow(_stmt, "completed")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfPhoneNumber: Int = getColumnIndexOrThrow(_stmt, "phoneNumber")
        val _columnIndexOfEstimatedTimeMinutes: Int = getColumnIndexOrThrow(_stmt, "estimatedTimeMinutes")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfStartTimeMillis: Int = getColumnIndexOrThrow(_stmt, "startTimeMillis")
        val _columnIndexOfEndTimeMillis: Int = getColumnIndexOrThrow(_stmt, "endTimeMillis")
        val _result: MutableList<TaskEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TaskEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDateMillis: Long
          _tmpDateMillis = _stmt.getLong(_columnIndexOfDateMillis)
          val _tmpDueMillis: Long?
          if (_stmt.isNull(_columnIndexOfDueMillis)) {
            _tmpDueMillis = null
          } else {
            _tmpDueMillis = _stmt.getLong(_columnIndexOfDueMillis)
          }
          val _tmpCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfCompleted).toInt()
          _tmpCompleted = _tmp != 0
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpPhoneNumber: String?
          if (_stmt.isNull(_columnIndexOfPhoneNumber)) {
            _tmpPhoneNumber = null
          } else {
            _tmpPhoneNumber = _stmt.getText(_columnIndexOfPhoneNumber)
          }
          val _tmpEstimatedTimeMinutes: Int
          _tmpEstimatedTimeMinutes = _stmt.getLong(_columnIndexOfEstimatedTimeMinutes).toInt()
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpStartTimeMillis: Long
          _tmpStartTimeMillis = _stmt.getLong(_columnIndexOfStartTimeMillis)
          val _tmpEndTimeMillis: Long
          _tmpEndTimeMillis = _stmt.getLong(_columnIndexOfEndTimeMillis)
          _item = TaskEntity(_tmpId,_tmpTitle,_tmpDateMillis,_tmpDueMillis,_tmpCompleted,_tmpType,_tmpNotes,_tmpPhoneNumber,_tmpEstimatedTimeMinutes,_tmpStatus,_tmpStartTimeMillis,_tmpEndTimeMillis)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAll() {
    val _sql: String = "DELETE FROM tasks"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
