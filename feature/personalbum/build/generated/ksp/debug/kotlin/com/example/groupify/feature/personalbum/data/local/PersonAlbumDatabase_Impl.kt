package com.example.groupify.feature.personalbum.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.example.groupify.feature.personalbum.`data`.local.dao.FaceEmbeddingDao
import com.example.groupify.feature.personalbum.`data`.local.dao.FaceEmbeddingDao_Impl
import com.example.groupify.feature.personalbum.`data`.local.dao.PersonDao
import com.example.groupify.feature.personalbum.`data`.local.dao.PersonDao_Impl
import com.example.groupify.feature.personalbum.`data`.local.dao.PhotoDao
import com.example.groupify.feature.personalbum.`data`.local.dao.PhotoDao_Impl
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

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(3,
        "9011e8ec392d99e7de78372979775738", "f6d07c4e65e433de7e265426dd691251") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `photos` (`id` TEXT NOT NULL, `uri` TEXT NOT NULL, `dateTaken` INTEGER NOT NULL, `lastIndexedAt` INTEGER, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `face_embeddings` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `photoId` TEXT NOT NULL, `left` REAL NOT NULL, `top` REAL NOT NULL, `right` REAL NOT NULL, `bottom` REAL NOT NULL, `embeddingBlob` BLOB NOT NULL, `createdAt` INTEGER NOT NULL)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_face_embeddings_photoId` ON `face_embeddings` (`photoId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `persons` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `referenceEmbeddingBlob` BLOB NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '9011e8ec392d99e7de78372979775738')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `photos`")
        connection.execSQL("DROP TABLE IF EXISTS `face_embeddings`")
        connection.execSQL("DROP TABLE IF EXISTS `persons`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsPhotos: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPhotos.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPhotos.put("uri", TableInfo.Column("uri", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPhotos.put("dateTaken", TableInfo.Column("dateTaken", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPhotos.put("lastIndexedAt", TableInfo.Column("lastIndexedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPhotos: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesPhotos: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoPhotos: TableInfo = TableInfo("photos", _columnsPhotos, _foreignKeysPhotos,
            _indicesPhotos)
        val _existingPhotos: TableInfo = read(connection, "photos")
        if (!_infoPhotos.equals(_existingPhotos)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |photos(com.example.groupify.feature.personalbum.data.local.entity.PhotoEntity).
              | Expected:
              |""".trimMargin() + _infoPhotos + """
              |
              | Found:
              |""".trimMargin() + _existingPhotos)
        }
        val _columnsFaceEmbeddings: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsFaceEmbeddings.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFaceEmbeddings.put("photoId", TableInfo.Column("photoId", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFaceEmbeddings.put("left", TableInfo.Column("left", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFaceEmbeddings.put("top", TableInfo.Column("top", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFaceEmbeddings.put("right", TableInfo.Column("right", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFaceEmbeddings.put("bottom", TableInfo.Column("bottom", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFaceEmbeddings.put("embeddingBlob", TableInfo.Column("embeddingBlob", "BLOB", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFaceEmbeddings.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysFaceEmbeddings: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesFaceEmbeddings: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesFaceEmbeddings.add(TableInfo.Index("index_face_embeddings_photoId", false,
            listOf("photoId"), listOf("ASC")))
        val _infoFaceEmbeddings: TableInfo = TableInfo("face_embeddings", _columnsFaceEmbeddings,
            _foreignKeysFaceEmbeddings, _indicesFaceEmbeddings)
        val _existingFaceEmbeddings: TableInfo = read(connection, "face_embeddings")
        if (!_infoFaceEmbeddings.equals(_existingFaceEmbeddings)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |face_embeddings(com.example.groupify.feature.personalbum.data.local.entity.FaceEmbeddingEntity).
              | Expected:
              |""".trimMargin() + _infoFaceEmbeddings + """
              |
              | Found:
              |""".trimMargin() + _existingFaceEmbeddings)
        }
        val _columnsPersons: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPersons.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPersons.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPersons.put("referenceEmbeddingBlob", TableInfo.Column("referenceEmbeddingBlob",
            "BLOB", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPersons.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPersons: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesPersons: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoPersons: TableInfo = TableInfo("persons", _columnsPersons, _foreignKeysPersons,
            _indicesPersons)
        val _existingPersons: TableInfo = read(connection, "persons")
        if (!_infoPersons.equals(_existingPersons)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |persons(com.example.groupify.feature.personalbum.data.local.entity.PersonEntity).
              | Expected:
              |""".trimMargin() + _infoPersons + """
              |
              | Found:
              |""".trimMargin() + _existingPersons)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "photos", "face_embeddings",
        "persons")
  }

  public override fun clearAllTables() {
    super.performClear(false, "photos", "face_embeddings", "persons")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(PhotoDao::class, PhotoDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(FaceEmbeddingDao::class, FaceEmbeddingDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(PersonDao::class, PersonDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun photoDao(): PhotoDao = _photoDao.value

  public override fun faceEmbeddingDao(): FaceEmbeddingDao = _faceEmbeddingDao.value

  public override fun personDao(): PersonDao = _personDao.value
}
