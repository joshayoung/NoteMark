package com.joshayoung.notemark.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: Int? = null,

    val content: String,
    val createdAt: String,
    val lastEditedAt: String? = null,
    val title: String,
)