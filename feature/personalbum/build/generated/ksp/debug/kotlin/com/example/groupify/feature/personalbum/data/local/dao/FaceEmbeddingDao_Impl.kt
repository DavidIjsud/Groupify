package com.example.groupify.feature.personalbum.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.groupify.feature.personalbum.`data`.local.entity.FaceEmbeddingEntity
import javax.`annotation`.processing.Generated
import kotlin.ByteArray
import kotlin.Float
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
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class FaceEmbeddingDao_Impl(
  __db: RoomDatabase,
) : FaceEmbeddingDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfFaceEmbeddingEntity: EntityInsertAdapter<FaceEmbeddingEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfFaceEmbeddingEntity = object : EntityInsertAdapter<FaceEmbeddingEntity>()
        {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `face_embeddings` (`id`,`photoId`,`left`,`top`,`right`,`bottom`,`embeddingBlob`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: FaceEmbeddingEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.photoId)
        statement.bindDouble(3, entity.left.toDouble())
        statement.bindDouble(4, entity.top.toDouble())
        statement.bindDouble(5, entity.right.toDouble())
        statement.bindDouble(6, entity.bottom.toDouble())
        statement.bindBlob(7, entity.embeddingBlob)
        statement.bindLong(8, entity.createdAt)
      }
    }
  }

  public override suspend fun insertAll(embeddings: List<FaceEmbeddingEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfFaceEmbeddingEntity.insert(_connection, embeddings)
  }

  public override fun getAllEmbeddings(): Flow<List<FaceEmbeddingEntity>> {
    val _sql: String = "SELECT * FROM face_embeddings"
    return createFlow(__db, false, arrayOf("face_embeddings")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPhotoId: Int = getColumnIndexOrThrow(_stmt, "photoId")
        val _columnIndexOfLeft: Int = getColumnIndexOrThrow(_stmt, "left")
        val _columnIndexOfTop: Int = getColumnIndexOrThrow(_stmt, "top")
        val _columnIndexOfRight: Int = getColumnIndexOrThrow(_stmt, "right")
        val _columnIndexOfBottom: Int = getColumnIndexOrThrow(_stmt, "bottom")
        val _columnIndexOfEmbeddingBlob: Int = getColumnIndexOrThrow(_stmt, "embeddingBlob")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<FaceEmbeddingEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FaceEmbeddingEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpPhotoId: String
          _tmpPhotoId = _stmt.getText(_columnIndexOfPhotoId)
          val _tmpLeft: Float
          _tmpLeft = _stmt.getDouble(_columnIndexOfLeft).toFloat()
          val _tmpTop: Float
          _tmpTop = _stmt.getDouble(_columnIndexOfTop).toFloat()
          val _tmpRight: Float
          _tmpRight = _stmt.getDouble(_columnIndexOfRight).toFloat()
          val _tmpBottom: Float
          _tmpBottom = _stmt.getDouble(_columnIndexOfBottom).toFloat()
          val _tmpEmbeddingBlob: ByteArray
          _tmpEmbeddingBlob = _stmt.getBlob(_columnIndexOfEmbeddingBlob)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item =
              FaceEmbeddingEntity(_tmpId,_tmpPhotoId,_tmpLeft,_tmpTop,_tmpRight,_tmpBottom,_tmpEmbeddingBlob,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getEmbeddingsForPhoto(photoId: String): List<FaceEmbeddingEntity> {
    val _sql: String = "SELECT * FROM face_embeddings WHERE photoId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, photoId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPhotoId: Int = getColumnIndexOrThrow(_stmt, "photoId")
        val _columnIndexOfLeft: Int = getColumnIndexOrThrow(_stmt, "left")
        val _columnIndexOfTop: Int = getColumnIndexOrThrow(_stmt, "top")
        val _columnIndexOfRight: Int = getColumnIndexOrThrow(_stmt, "right")
        val _columnIndexOfBottom: Int = getColumnIndexOrThrow(_stmt, "bottom")
        val _columnIndexOfEmbeddingBlob: Int = getColumnIndexOrThrow(_stmt, "embeddingBlob")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<FaceEmbeddingEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FaceEmbeddingEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpPhotoId: String
          _tmpPhotoId = _stmt.getText(_columnIndexOfPhotoId)
          val _tmpLeft: Float
          _tmpLeft = _stmt.getDouble(_columnIndexOfLeft).toFloat()
          val _tmpTop: Float
          _tmpTop = _stmt.getDouble(_columnIndexOfTop).toFloat()
          val _tmpRight: Float
          _tmpRight = _stmt.getDouble(_columnIndexOfRight).toFloat()
          val _tmpBottom: Float
          _tmpBottom = _stmt.getDouble(_columnIndexOfBottom).toFloat()
          val _tmpEmbeddingBlob: ByteArray
          _tmpEmbeddingBlob = _stmt.getBlob(_columnIndexOfEmbeddingBlob)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item =
              FaceEmbeddingEntity(_tmpId,_tmpPhotoId,_tmpLeft,_tmpTop,_tmpRight,_tmpBottom,_tmpEmbeddingBlob,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getEmbeddingsForPhotoIds(photoIds: List<String>):
      List<FaceEmbeddingEntity> {
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT * FROM face_embeddings WHERE photoId IN (")
    val _inputSize: Int = photoIds.size
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        for (_item: String in photoIds) {
          _stmt.bindText(_argIndex, _item)
          _argIndex++
        }
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPhotoId: Int = getColumnIndexOrThrow(_stmt, "photoId")
        val _columnIndexOfLeft: Int = getColumnIndexOrThrow(_stmt, "left")
        val _columnIndexOfTop: Int = getColumnIndexOrThrow(_stmt, "top")
        val _columnIndexOfRight: Int = getColumnIndexOrThrow(_stmt, "right")
        val _columnIndexOfBottom: Int = getColumnIndexOrThrow(_stmt, "bottom")
        val _columnIndexOfEmbeddingBlob: Int = getColumnIndexOrThrow(_stmt, "embeddingBlob")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<FaceEmbeddingEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item_1: FaceEmbeddingEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpPhotoId: String
          _tmpPhotoId = _stmt.getText(_columnIndexOfPhotoId)
          val _tmpLeft: Float
          _tmpLeft = _stmt.getDouble(_columnIndexOfLeft).toFloat()
          val _tmpTop: Float
          _tmpTop = _stmt.getDouble(_columnIndexOfTop).toFloat()
          val _tmpRight: Float
          _tmpRight = _stmt.getDouble(_columnIndexOfRight).toFloat()
          val _tmpBottom: Float
          _tmpBottom = _stmt.getDouble(_columnIndexOfBottom).toFloat()
          val _tmpEmbeddingBlob: ByteArray
          _tmpEmbeddingBlob = _stmt.getBlob(_columnIndexOfEmbeddingBlob)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item_1 =
              FaceEmbeddingEntity(_tmpId,_tmpPhotoId,_tmpLeft,_tmpTop,_tmpRight,_tmpBottom,_tmpEmbeddingBlob,_tmpCreatedAt)
          _result.add(_item_1)
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
