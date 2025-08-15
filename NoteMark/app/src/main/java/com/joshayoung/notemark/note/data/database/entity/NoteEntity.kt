package com.joshayoung.notemark.note.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: Long? = null,

    val remoteId: String,
    val content: String,
    val createdAt: String,
    val lastEditedAt: String? = null,
    val title: String,
)