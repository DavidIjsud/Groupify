package com.palmyrasoft.groupify.feature.personalbum.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.palmyrasoft.groupify.feature.personalbum.`data`.local.entity.PhotoEmbeddingEntity
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
public class PhotoEmbeddingDao_Impl(
  __db: RoomDatabase,
) : PhotoEmbeddingDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPhotoEmbeddingEntity: EntityInsertAdapter<PhotoEmbeddingEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfPhotoEmbeddingEntity = object : EntityInsertAdapter<PhotoEmbeddingEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `photo_embeddings` (`photoId`,`uri`,`embeddingBlob`,`createdAt`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PhotoEmbeddingEntity) {
        statement.bindText(1, entity.photoId)
        statement.bindText(2, entity.uri)
        statement.bindBlob(3, entity.embeddingBlob)
        statement.bindLong(4, entity.createdAt)
      }
    }
  }

  public override suspend fun insertAll(embeddings: List<PhotoEmbeddingEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfPhotoEmbeddingEntity.insert(_connection, embeddings)
  }

  public override fun getAllEmbeddings(): Flow<List<PhotoEmbeddingEntity>> {
    val _sql: String = "SELECT * FROM photo_embeddings"
    return createFlow(__db, false, arrayOf("photo_embeddings")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfPhotoId: Int = getColumnIndexOrThrow(_stmt, "photoId")
        val _columnIndexOfUri: Int = getColumnIndexOrThrow(_stmt, "uri")
        val _columnIndexOfEmbeddingBlob: Int = getColumnIndexOrThrow(_stmt, "embeddingBlob")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<PhotoEmbeddingEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PhotoEmbeddingEntity
          val _tmpPhotoId: String
          _tmpPhotoId = _stmt.getText(_columnIndexOfPhotoId)
          val _tmpUri: String
          _tmpUri = _stmt.getText(_columnIndexOfUri)
          val _tmpEmbeddingBlob: ByteArray
          _tmpEmbeddingBlob = _stmt.getBlob(_columnIndexOfEmbeddingBlob)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item = PhotoEmbeddingEntity(_tmpPhotoId,_tmpUri,_tmpEmbeddingBlob,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun countFlow(): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM photo_embeddings"
    return createFlow(__db, false, arrayOf("photo_embeddings")) { _connection ->
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
