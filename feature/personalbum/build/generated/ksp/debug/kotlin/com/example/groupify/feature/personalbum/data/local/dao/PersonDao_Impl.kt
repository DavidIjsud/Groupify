package com.example.groupify.feature.personalbum.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.groupify.feature.personalbum.`data`.local.entity.PersonEntity
import javax.`annotation`.processing.Generated
import kotlin.ByteArray
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class PersonDao_Impl(
  __db: RoomDatabase,
) : PersonDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPersonEntity: EntityInsertAdapter<PersonEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfPersonEntity = object : EntityInsertAdapter<PersonEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `persons` (`id`,`name`,`referenceEmbeddingBlob`,`createdAt`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PersonEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindBlob(3, entity.referenceEmbeddingBlob)
        statement.bindLong(4, entity.createdAt)
      }
    }
  }

  public override suspend fun insert(person: PersonEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfPersonEntity.insert(_connection, person)
  }

  public override fun getById(id: String): Flow<PersonEntity?> {
    val _sql: String = "SELECT * FROM persons WHERE id = ?"
    return createFlow(__db, false, arrayOf("persons")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfReferenceEmbeddingBlob: Int = getColumnIndexOrThrow(_stmt,
            "referenceEmbeddingBlob")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: PersonEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpReferenceEmbeddingBlob: ByteArray
          _tmpReferenceEmbeddingBlob = _stmt.getBlob(_columnIndexOfReferenceEmbeddingBlob)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _result = PersonEntity(_tmpId,_tmpName,_tmpReferenceEmbeddingBlob,_tmpCreatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAll(): Flow<List<PersonEntity>> {
    val _sql: String = "SELECT * FROM persons ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("persons")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfReferenceEmbeddingBlob: Int = getColumnIndexOrThrow(_stmt,
            "referenceEmbeddingBlob")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<PersonEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PersonEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpReferenceEmbeddingBlob: ByteArray
          _tmpReferenceEmbeddingBlob = _stmt.getBlob(_columnIndexOfReferenceEmbeddingBlob)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item = PersonEntity(_tmpId,_tmpName,_tmpReferenceEmbeddingBlob,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
