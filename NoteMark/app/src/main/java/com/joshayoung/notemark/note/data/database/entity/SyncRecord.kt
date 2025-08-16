package com.joshayoung.notemark.note.data.database.entity

import java.util.UUID

data class SyncRecord(
    val id: UUID,
    val userId: String,
    val noteId: String,
    val operation: SyncOperation,
    val payload: String,
    val timestamp: Long
)
