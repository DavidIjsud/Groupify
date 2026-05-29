package com.palmyrasoft.groupify.feature.personalbum.`data`.local.dao

import androidx.collection.ArrayMap
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndex
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.room.util.recursiveFetchArrayMap
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteStatement
import com.palmyrasoft.groupify.feature.personalbum.`data`.local.entity.GroupEntity
import com.palmyrasoft.groupify.feature.personalbum.`data`.local.entity.GroupPhotoEntity
import com.palmyrasoft.groupify.feature.personalbum.`data`.local.relation.GroupWithPhotos
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlin.text.StringBuilder
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class GroupDao_Impl(
  __db: RoomDatabase,
) : GroupDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfGroupEntity: EntityInsertAdapter<GroupEntity>

  private val __insertAdapterOfGroupPhotoEntity: EntityInsertAdapter<GroupPhotoEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfGroupEntity = object : EntityInsertAdapter<GroupEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `groups` (`id`,`name`,`faceCount`,`createdAt`,`updatedAt`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: GroupEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindLong(3, entity.faceCount.toLong())
        statement.bindLong(4, entity.createdAt)
        statement.bindLong(5, entity.updatedAt)
      }
    }
    this.__insertAdapterOfGroupPhotoEntity = object : EntityInsertAdapter<GroupPhotoEntity>() {
      protected override fun createQuery(): String = "INSERT OR IGNORE INTO `group_photos` (`id`,`groupId`,`photoUri`,`addedAt`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: GroupPhotoEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.groupId)
        statement.bindText(3, entity.photoUri)
        statement.bindLong(4, entity.addedAt)
      }
    }
  }

  public override suspend fun insertGroup(group: GroupEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfGroupEntity.insert(_connection, group)
  }

  public override suspend fun insertPhotos(photos: List<GroupPhotoEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfGroupPhotoEntity.insert(_connection, photos)
  }

  public override fun getGroupsWithPhotos(): Flow<List<GroupWithPhotos>> {
    val _sql: String = "SELECT * FROM groups ORDER BY updatedAt DESC"
    return createFlow(__db, true, arrayOf("group_photos", "groups")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfFaceCount: Int = getColumnIndexOrThrow(_stmt, "faceCount")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _collectionPhotos: ArrayMap<String, MutableList<GroupPhotoEntity>> = ArrayMap<String, MutableList<GroupPhotoEntity>>()
        while (_stmt.step()) {
          val _tmpKey: String
          _tmpKey = _stmt.getText(_columnIndexOfId)
          if (!_collectionPhotos.containsKey(_tmpKey)) {
            _collectionPhotos.put(_tmpKey, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshipgroupPhotosAscomPalmyrasoftGroupifyFeaturePersonalbumDataLocalEntityGroupPhotoEntity(_connection, _collectionPhotos)
        val _result: MutableList<GroupWithPhotos> = mutableListOf()
        while (_stmt.step()) {
          val _item: GroupWithPhotos
          val _tmpGroup: GroupEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpFaceCount: Int
          _tmpFaceCount = _stmt.getLong(_columnIndexOfFaceCount).toInt()
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          _tmpGroup = GroupEntity(_tmpId,_tmpName,_tmpFaceCount,_tmpCreatedAt,_tmpUpdatedAt)
          val _tmpPhotosCollection: MutableList<GroupPhotoEntity>
          val _tmpKey_1: String
          _tmpKey_1 = _stmt.getText(_columnIndexOfId)
          _tmpPhotosCollection = _collectionPhotos.getValue(_tmpKey_1)
          _item = GroupWithPhotos(_tmpGroup,_tmpPhotosCollection)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getGroupWithPhotos(groupId: String): Flow<GroupWithPhotos?> {
    val _sql: String = "SELECT * FROM groups WHERE id = ?"
    return createFlow(__db, true, arrayOf("group_photos", "groups")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, groupId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfFaceCount: Int = getColumnIndexOrThrow(_stmt, "faceCount")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _collectionPhotos: ArrayMap<String, MutableList<GroupPhotoEntity>> = ArrayMap<String, MutableList<GroupPhotoEntity>>()
        while (_stmt.step()) {
          val _tmpKey: String
          _tmpKey = _stmt.getText(_columnIndexOfId)
          if (!_collectionPhotos.containsKey(_tmpKey)) {
            _collectionPhotos.put(_tmpKey, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshipgroupPhotosAscomPalmyrasoftGroupifyFeaturePersonalbumDataLocalEntityGroupPhotoEntity(_connection, _collectionPhotos)
        val _result: GroupWithPhotos?
        if (_stmt.step()) {
          val _tmpGroup: GroupEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpFaceCount: Int
          _tmpFaceCount = _stmt.getLong(_columnIndexOfFaceCount).toInt()
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          _tmpGroup = GroupEntity(_tmpId,_tmpName,_tmpFaceCount,_tmpCreatedAt,_tmpUpdatedAt)
          val _tmpPhotosCollection: MutableList<GroupPhotoEntity>
          val _tmpKey_1: String
          _tmpKey_1 = _stmt.getText(_columnIndexOfId)
          _tmpPhotosCollection = _collectionPhotos.getValue(_tmpKey_1)
          _result = GroupWithPhotos(_tmpGroup,_tmpPhotosCollection)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getGroupWithPhotosOnce(groupId: String): GroupWithPhotos? {
    val _sql: String = "SELECT * FROM groups WHERE id = ?"
    return performSuspending(__db, true, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, groupId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfFaceCount: Int = getColumnIndexOrThrow(_stmt, "faceCount")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _collectionPhotos: ArrayMap<String, MutableList<GroupPhotoEntity>> = ArrayMap<String, MutableList<GroupPhotoEntity>>()
        while (_stmt.step()) {
          val _tmpKey: String
          _tmpKey = _stmt.getText(_columnIndexOfId)
          if (!_collectionPhotos.containsKey(_tmpKey)) {
            _collectionPhotos.put(_tmpKey, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshipgroupPhotosAscomPalmyrasoftGroupifyFeaturePersonalbumDataLocalEntityGroupPhotoEntity(_connection, _collectionPhotos)
        val _result: GroupWithPhotos?
        if (_stmt.step()) {
          val _tmpGroup: GroupEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpFaceCount: Int
          _tmpFaceCount = _stmt.getLong(_columnIndexOfFaceCount).toInt()
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          _tmpGroup = GroupEntity(_tmpId,_tmpName,_tmpFaceCount,_tmpCreatedAt,_tmpUpdatedAt)
          val _tmpPhotosCollection: MutableList<GroupPhotoEntity>
          val _tmpKey_1: String
          _tmpKey_1 = _stmt.getText(_columnIndexOfId)
          _tmpPhotosCollection = _collectionPhotos.getValue(_tmpKey_1)
          _result = GroupWithPhotos(_tmpGroup,_tmpPhotosCollection)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun touchGroup(
    groupId: String,
    updatedAt: Long,
    faceCount: Int,
  ) {
    val _sql: String = "UPDATE groups SET updatedAt = ?, faceCount = MAX(faceCount, ?) WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, faceCount.toLong())
        _argIndex = 3
        _stmt.bindText(_argIndex, groupId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteGroup(groupId: String) {
    val _sql: String = "DELETE FROM groups WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, groupId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  private fun __fetchRelationshipgroupPhotosAscomPalmyrasoftGroupifyFeaturePersonalbumDataLocalEntityGroupPhotoEntity(_connection: SQLiteConnection, _map: ArrayMap<String, MutableList<GroupPhotoEntity>>) {
    val __mapKeySet: Set<String> = _map.keys
    if (__mapKeySet.isEmpty()) {
      return
    }
    if (_map.size > 999) {
      recursiveFetchArrayMap(_map, true) { _tmpMap ->
        __fetchRelationshipgroupPhotosAscomPalmyrasoftGroupifyFeaturePersonalbumDataLocalEntityGroupPhotoEntity(_connection, _tmpMap)
      }
      return
    }
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT `id`,`groupId`,`photoUri`,`addedAt` FROM `group_photos` WHERE `groupId` IN (")
    val _inputSize: Int = __mapKeySet.size
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    val _stmt: SQLiteStatement = _connection.prepare(_sql)
    var _argIndex: Int = 1
    for (_item: String in __mapKeySet) {
      _stmt.bindText(_argIndex, _item)
      _argIndex++
    }
    try {
      val _itemKeyIndex: Int = getColumnIndex(_stmt, "groupId")
      if (_itemKeyIndex == -1) {
        return
      }
      val _columnIndexOfId: Int = 0
      val _columnIndexOfGroupId: Int = 1
      val _columnIndexOfPhotoUri: Int = 2
      val _columnIndexOfAddedAt: Int = 3
      while (_stmt.step()) {
        val _tmpKey: String
        _tmpKey = _stmt.getText(_itemKeyIndex)
        val _tmpRelation: MutableList<GroupPhotoEntity>? = _map.get(_tmpKey)
        if (_tmpRelation != null) {
          val _item_1: GroupPhotoEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpGroupId: String
          _tmpGroupId = _stmt.getText(_columnIndexOfGroupId)
          val _tmpPhotoUri: String
          _tmpPhotoUri = _stmt.getText(_columnIndexOfPhotoUri)
          val _tmpAddedAt: Long
          _tmpAddedAt = _stmt.getLong(_columnIndexOfAddedAt)
          _item_1 = GroupPhotoEntity(_tmpId,_tmpGroupId,_tmpPhotoUri,_tmpAddedAt)
          _tmpRelation.add(_item_1)
        }
      }
    } finally {
      _stmt.close()
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
