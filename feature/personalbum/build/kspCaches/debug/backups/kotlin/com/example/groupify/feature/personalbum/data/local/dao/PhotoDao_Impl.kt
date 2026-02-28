package com.example.groupify.feature.personalbum.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.groupify.feature.personalbum.`data`.local.entity.PhotoEntity
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlin.text.StringBuilder

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class PhotoDao_Impl(
  __db: RoomDatabase,
) : PhotoDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPhotoEntity: EntityInsertAdapter<PhotoEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfPhotoEntity = object : EntityInsertAdapter<PhotoEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR IGNORE INTO `photos` (`id`,`uri`,`dateTaken`,`lastIndexedAt`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PhotoEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.uri)
        statement.bindLong(3, entity.dateTaken)
        val _tmpLastIndexedAt: Long? = entity.lastIndexedAt
        if (_tmpLastIndexedAt == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmpLastIndexedAt)
        }
      }
    }
  }

  public override suspend fun upsertPhotos(photos: List<PhotoEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfPhotoEntity.insert(_connection, photos)
  }

  public override suspend fun getUnindexedPhotos(limit: Int): List<PhotoEntity> {
    val _sql: String = "SELECT * FROM photos WHERE lastIndexedAt IS NULL LIMIT ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUri: Int = getColumnIndexOrThrow(_stmt, "uri")
        val _columnIndexOfDateTaken: Int = getColumnIndexOrThrow(_stmt, "dateTaken")
        val _columnIndexOfLastIndexedAt: Int = getColumnIndexOrThrow(_stmt, "lastIndexedAt")
        val _result: MutableList<PhotoEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PhotoEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpUri: String
          _tmpUri = _stmt.getText(_columnIndexOfUri)
          val _tmpDateTaken: Long
          _tmpDateTaken = _stmt.getLong(_columnIndexOfDateTaken)
          val _tmpLastIndexedAt: Long?
          if (_stmt.isNull(_columnIndexOfLastIndexedAt)) {
            _tmpLastIndexedAt = null
          } else {
            _tmpLastIndexedAt = _stmt.getLong(_columnIndexOfLastIndexedAt)
          }
          _item = PhotoEntity(_tmpId,_tmpUri,_tmpDateTaken,_tmpLastIndexedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getPhotoById(photoId: String): PhotoEntity? {
    val _sql: String = "SELECT * FROM photos WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, photoId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUri: Int = getColumnIndexOrThrow(_stmt, "uri")
        val _columnIndexOfDateTaken: Int = getColumnIndexOrThrow(_stmt, "dateTaken")
        val _columnIndexOfLastIndexedAt: Int = getColumnIndexOrThrow(_stmt, "lastIndexedAt")
        val _result: PhotoEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpUri: String
          _tmpUri = _stmt.getText(_columnIndexOfUri)
          val _tmpDateTaken: Long
          _tmpDateTaken = _stmt.getLong(_columnIndexOfDateTaken)
          val _tmpLastIndexedAt: Long?
          if (_stmt.isNull(_columnIndexOfLastIndexedAt)) {
            _tmpLastIndexedAt = null
          } else {
            _tmpLastIndexedAt = _stmt.getLong(_columnIndexOfLastIndexedAt)
          }
          _result = PhotoEntity(_tmpId,_tmpUri,_tmpDateTaken,_tmpLastIndexedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getByIds(ids: List<String>): List<PhotoEntity> {
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT * FROM photos WHERE id IN (")
    val _inputSize: Int = ids.size
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        for (_item: String in ids) {
          _stmt.bindText(_argIndex, _item)
          _argIndex++
        }
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUri: Int = getColumnIndexOrThrow(_stmt, "uri")
        val _columnIndexOfDateTaken: Int = getColumnIndexOrThrow(_stmt, "dateTaken")
        val _columnIndexOfLastIndexedAt: Int = getColumnIndexOrThrow(_stmt, "lastIndexedAt")
        val _result: MutableList<PhotoEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item_1: PhotoEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpUri: String
          _tmpUri = _stmt.getText(_columnIndexOfUri)
          val _tmpDateTaken: Long
          _tmpDateTaken = _stmt.getLong(_columnIndexOfDateTaken)
          val _tmpLastIndexedAt: Long?
          if (_stmt.isNull(_columnIndexOfLastIndexedAt)) {
            _tmpLastIndexedAt = null
          } else {
            _tmpLastIndexedAt = _stmt.getLong(_columnIndexOfLastIndexedAt)
          }
          _item_1 = PhotoEntity(_tmpId,_tmpUri,_tmpDateTaken,_tmpLastIndexedAt)
          _result.add(_item_1)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markPhotoIndexed(photoId: String, timestamp: Long) {
    val _sql: String = "UPDATE photos SET lastIndexedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, timestamp)
        _argIndex = 2
        _stmt.bindText(_argIndex, photoId)
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
