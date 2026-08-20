package com.example.mekanat_new.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChurchDao {
    @Query("SELECT * FROM churches WHERE isVerified = 1 ORDER BY name ASC")
    fun getAllVerified(): Flow<List<ChurchEntity>>

    @Query("SELECT * FROM churches WHERE isVerified = 0 ORDER BY createdAt DESC")
    fun getPendingSubmissions(): Flow<List<ChurchEntity>>

    @Query("SELECT * FROM churches WHERE id = :id")
    fun getById(id: Long): Flow<ChurchEntity?>

    @Query("SELECT * FROM churches WHERE id = :id LIMIT 1")
    suspend fun getByIdSync(id: Long): ChurchEntity?

    // unified search: church name/region/diocese OR any tabot name
    @Query("""
        SELECT DISTINCT c.* FROM churches c
        LEFT JOIN tabots t ON t.churchId = c.id
        WHERE c.isVerified = 1 AND (
            c.name LIKE '%' || :query || '%' OR
            c.nameAmharic LIKE '%' || :query || '%' OR
            c.region LIKE '%' || :query || '%' OR
            c.diocese LIKE '%' || :query || '%' OR
            t.name LIKE '%' || :query || '%' OR
            t.nameEnglish LIKE '%' || :query || '%'
        )
    """)
    fun search(query: String): Flow<List<ChurchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(church: ChurchEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(churches: List<ChurchEntity>)

    @Update
    suspend fun update(church: ChurchEntity)

    @Delete
    suspend fun delete(church: ChurchEntity)
}

@Dao
interface TabotDao {
    @Query("SELECT * FROM tabots WHERE churchId = :churchId")
    fun getForChurch(churchId: Long): Flow<List<TabotEntity>>

    @Query("SELECT * FROM tabots WHERE churchId = :churchId")
    suspend fun getForChurchSync(churchId: Long): List<TabotEntity>

    @Query("SELECT * FROM tabots WHERE nameEnglish LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%'")
    fun searchTabots(query: String): Flow<List<TabotEntity>>

    @Query("SELECT * FROM tabots")
    fun getAll(): Flow<List<TabotEntity>>

    @Query("SELECT * FROM tabots")
    suspend fun getAllSync(): List<TabotEntity>

    @Query("SELECT * FROM tabots WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): TabotEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tabot: TabotEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tabots: List<TabotEntity>)
}

@Dao
interface GubaeDao {
    @Query("SELECT * FROM gubae_events WHERE isActive = 1 AND churchId = :churchId ORDER BY startDateEpoch ASC")
    fun getActiveForChurch(churchId: Long): Flow<List<GubaeEventEntity>>

    @Query("SELECT * FROM gubae_events WHERE churchId = :churchId ORDER BY startDateEpoch DESC")
    fun getHistoryForChurch(churchId: Long): Flow<List<GubaeEventEntity>>

    @Query("SELECT * FROM gubae_events WHERE isActive = 1")
    fun getAllActive(): Flow<List<GubaeEventEntity>>

    @Query("SELECT * FROM gubae_events WHERE isActive = 1")
    suspend fun getAllActiveSync(): List<GubaeEventEntity>

    @Query("UPDATE gubae_events SET isActive = 0 WHERE isActive = 1 AND endDateEpoch < :nowEpoch")
    suspend fun archiveExpired(nowEpoch: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: GubaeEventEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<GubaeEventEntity>)
}

@Dao
interface FavoriteDao {
    @Query("SELECT c.* FROM churches c INNER JOIN favorites f ON f.churchId = c.id ORDER BY f.savedAt DESC")
    fun getFavoriteChurches(): Flow<List<ChurchEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE churchId = :churchId)")
    fun isFavorite(churchId: Long): Flow<Boolean>

    @Query("SELECT churchId FROM favorites")
    fun getAllFavoriteIds(): Flow<List<Long>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(fav: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE churchId = :churchId")
    suspend fun remove(churchId: Long)
}

@Dao
interface SavedNigsDao {
    @Query("SELECT * FROM saved_nigs ORDER BY savedAt DESC")
    fun getAllSavedNigs(): Flow<List<SavedNigsEntity>>

    @Query("SELECT tabotId FROM saved_nigs")
    fun getAllSavedTabotIds(): Flow<List<Long>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_nigs WHERE tabotId = :tabotId)")
    fun isNigsSaved(tabotId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(nigs: SavedNigsEntity)

    @Query("DELETE FROM saved_nigs WHERE tabotId = :tabotId")
    suspend fun remove(tabotId: Long)
}

@Dao
interface ChurchPhotoDao {
    @Query("SELECT * FROM church_photos WHERE churchId = :churchId AND isApproved = 1 ORDER BY uploadedAt DESC")
    fun getPhotosForChurch(churchId: Long): Flow<List<ChurchPhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: ChurchPhotoEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<ChurchPhotoEntity>)
}

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT 12")
    fun getRecentSearches(): Flow<List<SearchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(entry: SearchHistoryEntity): Long

    @Query("DELETE FROM search_history WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM search_history")
    suspend fun clearAll()
}
