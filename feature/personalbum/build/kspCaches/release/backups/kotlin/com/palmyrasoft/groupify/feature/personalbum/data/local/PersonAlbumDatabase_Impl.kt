package com.palmyrasoft.groupify.feature.personalbum.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.palmyrasoft.groupify.feature.personalbum.`data`.local.dao.FaceEmbeddingDao
import com.palmyrasoft.groupify.feature.personalbum.`data`.local.dao.FaceEmbeddingDao_Impl
import com.palmyrasoft.groupify.feature.personalbum.`data`.local.dao.GroupDao
import com.palmyrasoft.groupify.feature.personalbum.`data`.local.dao.GroupDao_Impl
import com.palmyrasoft.groupify.feature.personalbum.`data`.local.dao.PersonDao
import com.palmyrasoft.groupify.feature.personalbum.`data`.local.dao.PersonDao_Impl
import com.palmyrasoft.groupify.feature.personalbum.`data`.local.dao.PhotoDao
import com.palmyrasoft.groupify.feature.personalbum.`data`.local.dao.PhotoDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class PersonAlbumDatabase_Impl : PersonAlbumDatabase() {
  private val _photoDao: Lazy<PhotoDao> = lazy {
    PhotoDao_Impl(this)
  }

  private val _faceEmbeddingDao: Lazy<FaceEmbeddingDao> = lazy {
    FaceEmbeddingDao_Impl(this)
  }

  private val _personDao: Lazy<PersonDao> = lazy {
    PersonDao_Impl(this)
  }

  private val _groupDao: Lazy<GroupDao> = lazy {
    GroupDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(4, "5a05bf9597c8b8176d85810a79f8d8ae", "3022377357acb64d2baaedf07856915f") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `photos` (`id` TEXT NOT NULL, `uri` TEXT NOT NULL, `dateTaken` INTEGER NOT NULL, `lastIndexedAt` INTEGER, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `face_embeddings` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `photoId` TEXT NOT NULL, `left` REAL NOT NULL, `top` REAL NOT NULL, `right` REAL NOT NULL, `bottom` REAL NOT NULL, `embeddingBlob` BLOB NOT NULL, `createdAt` INTEGER NOT NULL)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_face_embeddings_photoId` ON `face_embeddings` (`photoId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `persons` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `referenceEmbeddingBlob` BLOB NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `groups` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `faceCount` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `group_photos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `groupId` TEXT NOT NULL, `photoUri` TEXT NOT NULL, `addedAt` INTEGER NOT NULL, FOREIGN KEY(`groupId`) REFERENCES `groups`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_group_photos_groupId` ON `group_photos` (`groupId`)")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_group_photos_groupId_photoUri` ON `group_photos` (`groupId`, `photoUri`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '5a05bf9597c8b8176d85810a79f8d8ae')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `photos`")
        connection.execSQL("DROP TABLE IF EXISTS `face_embeddings`")
        connection.execSQL("DROP TABLE IF EXISTS `persons`")
        connection.execSQL("DROP TABLE IF EXISTS `groups`")
        connection.execSQL("DROP TABLE IF EXISTS `group_photos`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsPhotos: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPhotos.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPhotos.put("uri", TableInfo.Column("uri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPhotos.put("dateTaken", TableInfo.Column("dateTaken", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPhotos.put("lastIndexedAt", TableInfo.Column("lastIndexedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPhotos: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesPhotos: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoPhotos: TableInfo = TableInfo("photos", _columnsPhotos, _foreignKeysPhotos, _indicesPhotos)
        val _existingPhotos: TableInfo = read(connection, "photos")
        if (!_infoPhotos.equals(_existingPhotos)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |photos(com.palmyrasoft.groupify.feature.personalbum.data.local.entity.PhotoEntity).
              | Expected:
              |""".trimMargin() + _infoPhotos + """
              |
              | Found:
              |""".trimMargin() + _existingPhotos)
        }
        val _columnsFaceEmbeddings: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsFaceEmbeddings.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFaceEmbeddings.put("photoId", TableInfo.Column("photoId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFaceEmbeddings.put("left", TableInfo.Column("left", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFaceEmbeddings.put("top", TableInfo.Column("top", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFaceEmbeddings.put("right", TableInfo.Column("right", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFaceEmbeddings.put("bottom", TableInfo.Column("bottom", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFaceEmbeddings.put("embeddingBlob", TableInfo.Column("embeddingBlob", "BLOB", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFaceEmbeddings.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysFaceEmbeddings: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesFaceEmbeddings: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesFaceEmbeddings.add(TableInfo.Index("index_face_embeddings_photoId", false, listOf("photoId"), listOf("ASC")))
        val _infoFaceEmbeddings: TableInfo = TableInfo("face_embeddings", _columnsFaceEmbeddings, _foreignKeysFaceEmbeddings, _indicesFaceEmbeddings)
        val _existingFaceEmbeddings: TableInfo = read(connection, "face_embeddings")
        if (!_infoFaceEmbeddings.equals(_existingFaceEmbeddings)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |face_embeddings(com.palmyrasoft.groupify.feature.personalbum.data.local.entity.FaceEmbeddingEntity).
              | Expected:
              |""".trimMargin() + _infoFaceEmbeddings + """
              |
              | Found:
              |""".trimMargin() + _existingFaceEmbeddings)
        }
        val _columnsPersons: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPersons.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPersons.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPersons.put("referenceEmbeddingBlob", TableInfo.Column("referenceEmbeddingBlob", "BLOB", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPersons.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPersons: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesPersons: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoPersons: TableInfo = TableInfo("persons", _columnsPersons, _foreignKeysPersons, _indicesPersons)
        val _existingPersons: TableInfo = read(connection, "persons")
        if (!_infoPersons.equals(_existingPersons)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |persons(com.palmyrasoft.groupify.feature.personalbum.data.local.entity.PersonEntity).
              | Expected:
              |""".trimMargin() + _infoPersons + """
              |
              | Found:
              |""".trimMargin() + _existingPersons)
        }
        val _columnsGroups: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsGroups.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGroups.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGroups.put("faceCount", TableInfo.Column("faceCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGroups.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGroups.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysGroups: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesGroups: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoGroups: TableInfo = TableInfo("groups", _columnsGroups, _foreignKeysGroups, _indicesGroups)
        val _existingGroups: TableInfo = read(connection, "groups")
        if (!_infoGroups.equals(_existingGroups)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |groups(com.palmyrasoft.groupify.feature.personalbum.data.local.entity.GroupEntity).
              | Expected:
              |""".trimMargin() + _infoGroups + """
              |
              | Found:
              |""".trimMargin() + _existingGroups)
        }
        val _columnsGroupPhotos: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsGroupPhotos.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGroupPhotos.put("groupId", TableInfo.Column("groupId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGroupPhotos.put("photoUri", TableInfo.Column("photoUri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGroupPhotos.put("addedAt", TableInfo.Column("addedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysGroupPhotos: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysGroupPhotos.add(TableInfo.ForeignKey("groups", "CASCADE", "NO ACTION", listOf("groupId"), listOf("id")))
        val _indicesGroupPhotos: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesGroupPhotos.add(TableInfo.Index("index_group_photos_groupId", false, listOf("groupId"), listOf("ASC")))
        _indicesGroupPhotos.add(TableInfo.Index("index_group_photos_groupId_photoUri", true, listOf("groupId", "photoUri"), listOf("ASC", "ASC")))
        val _infoGroupPhotos: TableInfo = TableInfo("group_photos", _columnsGroupPhotos, _foreignKeysGroupPhotos, _indicesGroupPhotos)
        val _existingGroupPhotos: TableInfo = read(connection, "group_photos")
        if (!_infoGroupPhotos.equals(_existingGroupPhotos)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |group_photos(com.palmyrasoft.groupify.feature.personalbum.data.local.entity.GroupPhotoEntity).
              | Expected:
              |""".trimMargin() + _infoGroupPhotos + """
              |
              | Found:
              |""".trimMargin() + _existingGroupPhotos)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "photos", "face_embeddings", "persons", "groups", "group_photos")
  }

  public override fun clearAllTables() {
    super.performClear(true, "photos", "face_embeddings", "persons", "groups", "group_photos")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(PhotoDao::class, PhotoDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(FaceEmbeddingDao::class, FaceEmbeddingDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(PersonDao::class, PersonDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(GroupDao::class, GroupDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun photoDao(): PhotoDao = _photoDao.value

  public override fun faceEmbeddingDao(): FaceEmbeddingDao = _faceEmbeddingDao.value

  public override fun personDao(): PersonDao = _personDao.value

  public override fun groupDao(): GroupDao = _groupDao.value
}
