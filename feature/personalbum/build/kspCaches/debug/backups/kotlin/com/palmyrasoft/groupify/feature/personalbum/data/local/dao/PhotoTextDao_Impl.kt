package com.palmyrasoft.groupify.feature.personalbum.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.palmyrasoft.groupify.feature.personalbum.`data`.local.entity.PhotoTextEntity
import javax.`annotation`.processing.Generated
import kotlin.Int
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
public class PhotoTextDao_Impl(
  __db: RoomDatabase,
) : PhotoTextDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPhotoTextEntity: EntityInsertAdapter<PhotoTextEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfPhotoTextEntity = object : EntityInsertAdapter<PhotoTextEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `photo_texts` (`rowid`,`photoId`,`uri`,`text`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PhotoTextEntity) {
        statement.bindLong(1, entity.rowId.toLong())
        statement.bindText(2, entity.photoId)
        statement.bindText(3, entity.uri)
        statement.bindText(4, entity.text)
      }
    }
  }

  public override suspend fun insertAll(items: List<PhotoTextEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfPhotoTextEntity.insert(_connection, items)
  }

  public override suspend fun search(ftsQuery: String): List<String> {
    val _sql: String = "SELECT uri FROM photo_texts WHERE text MATCH ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, ftsQuery)
        val _result: MutableList<String> = mutableListOf()
        while (_stmt.step()) {
          val _item: String
          val _tmp: String
          _tmp = _stmt.getText(0)
          _item = _tmp
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun countFlow(): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM photo_texts"
    return createFlow(__db, false, arrayOf("photo_texts")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
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
