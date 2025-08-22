package com.joshayoung.notemark.note.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_queue")
data class SyncRecord(
    @PrimaryKey val id: Long? = null,
    val userId: String,
    val noteId: String?,
    val operation: SyncOperation,
    val payload: String,
    val timestamp: Long,
)
