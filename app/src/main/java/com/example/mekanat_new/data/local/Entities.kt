package com.example.mekanat_new.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "churches")
data class ChurchEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val nameAmharic: String? = null,
    val latitude: Double,
    val longitude: Double,
    val region: String,
    val diocese: String,
    val churchType: String,       // ROCK_HEWN, MONASTERY, CATHEDRAL, PARISH
    val description: String? = null,
    val history: String? = null,
    val address: String? = null,
    val contactPhone: String? = null,
    val contactEmail: String? = null,
    val isVerified: Boolean = true,   // false for community submissions pending review
    val submittedBy: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "tabots",
    foreignKeys = [ForeignKey(
        entity = ChurchEntity::class,
        parentColumns = ["id"],
        childColumns = ["churchId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("churchId"), Index("name")]
)
data class TabotEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val churchId: Long,
    val name: String,              // e.g. "ኪዳነ ምሕረት"
    val nameEnglish: String,       // "Kidane Mihret"
    val nigsMonth: Int,            // Ethiopian month 1-13
    val nigsDay: Int,              // Ethiopian day 1-30
    val description: String? = null,
    val routingDescription: String? = null
)

@Entity(
    tableName = "gubae_events",
    foreignKeys = [ForeignKey(
        entity = ChurchEntity::class,
        parentColumns = ["id"],
        childColumns = ["churchId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("churchId"), Index("endDateEpoch")]
)
data class GubaeEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val churchId: Long,
    val title: String,             // "Annual Gubae"
    val description: String?,
    val startDateEpoch: Long,
    val endDateEpoch: Long,
    val isActive: Boolean = true,  // flipped false by cleanup worker after endDate
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "favorites",
    primaryKeys = ["churchId"]
)
data class FavoriteEntity(
    val churchId: Long,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "saved_nigs",
    primaryKeys = ["tabotId"]
)
data class SavedNigsEntity(
    val tabotId: Long,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "church_photos",
    foreignKeys = [ForeignKey(
        entity = ChurchEntity::class,
        parentColumns = ["id"],
        childColumns = ["churchId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("churchId")]
)
data class ChurchPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val churchId: Long,
    val uri: String,
    val caption: String? = null,
    val uploadedBy: String? = null,
    val isApproved: Boolean = true,
    val uploadedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val query: String,
    val timestamp: Long = System.currentTimeMillis(),
    val resultCount: Int = 0
)

enum class SubmissionStatus { PENDING, APPROVED, REJECTED }
