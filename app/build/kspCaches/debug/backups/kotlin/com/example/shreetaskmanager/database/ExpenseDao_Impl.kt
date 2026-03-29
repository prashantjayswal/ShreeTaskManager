package com.example.shreetaskmanager.database

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.RoomRawQuery
import androidx.room.RoomSQLiteQuery
import androidx.room.util.getColumnIndex
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import androidx.sqlite.db.SupportSQLiteQuery
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Double
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
public class ExpenseDao_Impl(
  __db: RoomDatabase,
) : ExpenseDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfExpenseEntity: EntityInsertAdapter<ExpenseEntity>

  private val __updateAdapterOfExpenseEntity: EntityDeleteOrUpdateAdapter<ExpenseEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfExpenseEntity = object : EntityInsertAdapter<ExpenseEntity>() {
      protected override fun createQuery(): String = "INSERT OR IGNORE INTO `expenses` (`id`,`amount`,`description`,`dateMillis`,`isInvestment`,`category`,`source`,`transactionType`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ExpenseEntity) {
        statement.bindLong(1, entity.id)
        statement.bindDouble(2, entity.amount)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpDescription)
        }
        statement.bindLong(4, entity.dateMillis)
        val _tmp: Int = if (entity.isInvestment) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        statement.bindText(6, entity.category)
        statement.bindText(7, entity.source)
        statement.bindText(8, entity.transactionType)
      }
    }
    this.__updateAdapterOfExpenseEntity = object : EntityDeleteOrUpdateAdapter<ExpenseEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `expenses` SET `id` = ?,`amount` = ?,`description` = ?,`dateMillis` = ?,`isInvestment` = ?,`category` = ?,`source` = ?,`transactionType` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: ExpenseEntity) {
        statement.bindLong(1, entity.id)
        statement.bindDouble(2, entity.amount)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpDescription)
        }
        statement.bindLong(4, entity.dateMillis)
        val _tmp: Int = if (entity.isInvestment) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        statement.bindText(6, entity.category)
        statement.bindText(7, entity.source)
        statement.bindText(8, entity.transactionType)
        statement.bindLong(9, entity.id)
      }
    }
  }

  public override suspend fun insert(expense: ExpenseEntity): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfExpenseEntity.insertAndReturnId(_connection, expense)
    _result
  }

  public override suspend fun update(expense: ExpenseEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfExpenseEntity.handle(_connection, expense)
  }

  public override suspend fun expensesForDayRange(dayStart: Long, dayEnd: Long): List<ExpenseEntity> {
    val _sql: String = "SELECT * FROM expenses WHERE dateMillis >= ? AND dateMillis <= ? ORDER BY dateMillis DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, dayStart)
        _argIndex = 2
        _stmt.bindLong(_argIndex, dayEnd)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfDateMillis: Int = getColumnIndexOrThrow(_stmt, "dateMillis")
        val _columnIndexOfIsInvestment: Int = getColumnIndexOrThrow(_stmt, "isInvestment")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfSource: Int = getColumnIndexOrThrow(_stmt, "source")
        val _columnIndexOfTransactionType: Int = getColumnIndexOrThrow(_stmt, "transactionType")
        val _result: MutableList<ExpenseEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ExpenseEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_columnIndexOfAmount)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpDateMillis: Long
          _tmpDateMillis = _stmt.getLong(_columnIndexOfDateMillis)
          val _tmpIsInvestment: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsInvestment).toInt()
          _tmpIsInvestment = _tmp != 0
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpSource: String
          _tmpSource = _stmt.getText(_columnIndexOfSource)
          val _tmpTransactionType: String
          _tmpTransactionType = _stmt.getText(_columnIndexOfTransactionType)
          _item = ExpenseEntity(_tmpId,_tmpAmount,_tmpDescription,_tmpDateMillis,_tmpIsInvestment,_tmpCategory,_tmpSource,_tmpTransactionType)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun totalSpent(): Double? {
    val _sql: String = "SELECT SUM(amount) FROM expenses WHERE isInvestment = 0 AND transactionType = 'DEBIT'"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Double?
        if (_stmt.step()) {
          val _tmp: Double?
          if (_stmt.isNull(0)) {
            _tmp = null
          } else {
            _tmp = _stmt.getDouble(0)
          }
          _result = _tmp
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun totalInvested(): Double? {
    val _sql: String = "SELECT SUM(amount) FROM expenses WHERE isInvestment = 1 AND transactionType = 'DEBIT'"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Double?
        if (_stmt.step()) {
          val _tmp: Double?
          if (_stmt.isNull(0)) {
            _tmp = null
          } else {
            _tmp = _stmt.getDouble(0)
          }
          _result = _tmp
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun totalSpentInRange(start: Long, end: Long): Double? {
    val _sql: String = "SELECT SUM(amount) FROM expenses WHERE dateMillis >= ? AND dateMillis <= ? AND isInvestment = 0 AND transactionType = 'DEBIT'"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, start)
        _argIndex = 2
        _stmt.bindLong(_argIndex, end)
        val _result: Double?
        if (_stmt.step()) {
          val _tmp: Double?
          if (_stmt.isNull(0)) {
            _tmp = null
          } else {
            _tmp = _stmt.getDouble(0)
          }
          _result = _tmp
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun totalInvestedInRange(start: Long, end: Long): Double? {
    val _sql: String = "SELECT SUM(amount) FROM expenses WHERE dateMillis >= ? AND dateMillis <= ? AND isInvestment = 1 AND transactionType = 'DEBIT'"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, start)
        _argIndex = 2
        _stmt.bindLong(_argIndex, end)
        val _result: Double?
        if (_stmt.step()) {
          val _tmp: Double?
          if (_stmt.isNull(0)) {
            _tmp = null
          } else {
            _tmp = _stmt.getDouble(0)
          }
          _result = _tmp
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllExpenses(): List<ExpenseEntity> {
    val _sql: String = "SELECT * FROM expenses ORDER BY dateMillis DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfDateMillis: Int = getColumnIndexOrThrow(_stmt, "dateMillis")
        val _columnIndexOfIsInvestment: Int = getColumnIndexOrThrow(_stmt, "isInvestment")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfSource: Int = getColumnIndexOrThrow(_stmt, "source")
        val _columnIndexOfTransactionType: Int = getColumnIndexOrThrow(_stmt, "transactionType")
        val _result: MutableList<ExpenseEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ExpenseEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_columnIndexOfAmount)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpDateMillis: Long
          _tmpDateMillis = _stmt.getLong(_columnIndexOfDateMillis)
          val _tmpIsInvestment: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsInvestment).toInt()
          _tmpIsInvestment = _tmp != 0
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpSource: String
          _tmpSource = _stmt.getText(_columnIndexOfSource)
          val _tmpTransactionType: String
          _tmpTransactionType = _stmt.getText(_columnIndexOfTransactionType)
          _item = ExpenseEntity(_tmpId,_tmpAmount,_tmpDescription,_tmpDateMillis,_tmpIsInvestment,_tmpCategory,_tmpSource,_tmpTransactionType)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(id: Long): ExpenseEntity? {
    val _sql: String = "SELECT * FROM expenses WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfDateMillis: Int = getColumnIndexOrThrow(_stmt, "dateMillis")
        val _columnIndexOfIsInvestment: Int = getColumnIndexOrThrow(_stmt, "isInvestment")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfSource: Int = getColumnIndexOrThrow(_stmt, "source")
        val _columnIndexOfTransactionType: Int = getColumnIndexOrThrow(_stmt, "transactionType")
        val _result: ExpenseEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_columnIndexOfAmount)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpDateMillis: Long
          _tmpDateMillis = _stmt.getLong(_columnIndexOfDateMillis)
          val _tmpIsInvestment: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsInvestment).toInt()
          _tmpIsInvestment = _tmp != 0
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpSource: String
          _tmpSource = _stmt.getText(_columnIndexOfSource)
          val _tmpTransactionType: String
          _tmpTransactionType = _stmt.getText(_columnIndexOfTransactionType)
          _result = ExpenseEntity(_tmpId,_tmpAmount,_tmpDescription,_tmpDateMillis,_tmpIsInvestment,_tmpCategory,_tmpSource,_tmpTransactionType)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAll() {
    val _sql: String = "DELETE FROM expenses"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getExpensesFiltered(query: SupportSQLiteQuery): List<ExpenseEntity> {
    val _rawQuery: RoomRawQuery = RoomSQLiteQuery.copyFrom(query).toRoomRawQuery()
    val _sql: String = _rawQuery.sql
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _rawQuery.getBindingFunction().invoke(_stmt)
        val _result: MutableList<ExpenseEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ExpenseEntity
          _item = __entityStatementConverter_comExampleShreetaskmanagerDatabaseExpenseEntity(_stmt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  private fun __entityStatementConverter_comExampleShreetaskmanagerDatabaseExpenseEntity(statement: SQLiteStatement): ExpenseEntity {
    val _entity: ExpenseEntity
    val _columnIndexOfId: Int = getColumnIndex(statement, "id")
    val _columnIndexOfAmount: Int = getColumnIndex(statement, "amount")
    val _columnIndexOfDescription: Int = getColumnIndex(statement, "description")
    val _columnIndexOfDateMillis: Int = getColumnIndex(statement, "dateMillis")
    val _columnIndexOfIsInvestment: Int = getColumnIndex(statement, "isInvestment")
    val _columnIndexOfCategory: Int = getColumnIndex(statement, "category")
    val _columnIndexOfSource: Int = getColumnIndex(statement, "source")
    val _columnIndexOfTransactionType: Int = getColumnIndex(statement, "transactionType")
    val _tmpId: Long
    if (_columnIndexOfId == -1) {
      _tmpId = 0
    } else {
      _tmpId = statement.getLong(_columnIndexOfId)
    }
    val _tmpAmount: Double
    if (_columnIndexOfAmount == -1) {
      _tmpAmount = 0.0
    } else {
      _tmpAmount = statement.getDouble(_columnIndexOfAmount)
    }
    val _tmpDescription: String?
    if (_columnIndexOfDescription == -1) {
      _tmpDescription = null
    } else {
      if (statement.isNull(_columnIndexOfDescription)) {
        _tmpDescription = null
      } else {
        _tmpDescription = statement.getText(_columnIndexOfDescription)
      }
    }
    val _tmpDateMillis: Long
    if (_columnIndexOfDateMillis == -1) {
      _tmpDateMillis = 0
    } else {
      _tmpDateMillis = statement.getLong(_columnIndexOfDateMillis)
    }
    val _tmpIsInvestment: Boolean
    if (_columnIndexOfIsInvestment == -1) {
      _tmpIsInvestment = false
    } else {
      val _tmp: Int
      _tmp = statement.getLong(_columnIndexOfIsInvestment).toInt()
      _tmpIsInvestment = _tmp != 0
    }
    val _tmpCategory: String
    if (_columnIndexOfCategory == -1) {
      error("Missing column 'category' for a NON-NULL value, column not found in result.")
    } else {
      _tmpCategory = statement.getText(_columnIndexOfCategory)
    }
    val _tmpSource: String
    if (_columnIndexOfSource == -1) {
      error("Missing column 'source' for a NON-NULL value, column not found in result.")
    } else {
      _tmpSource = statement.getText(_columnIndexOfSource)
    }
    val _tmpTransactionType: String
    if (_columnIndexOfTransactionType == -1) {
      error("Missing column 'transactionType' for a NON-NULL value, column not found in result.")
    } else {
      _tmpTransactionType = statement.getText(_columnIndexOfTransactionType)
    }
    _entity = ExpenseEntity(_tmpId,_tmpAmount,_tmpDescription,_tmpDateMillis,_tmpIsInvestment,_tmpCategory,_tmpSource,_tmpTransactionType)
    return _entity
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
