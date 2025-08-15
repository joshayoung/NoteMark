package com.joshayoung.notemark.note.domain.models

data class Note(
    val id: Long? = null,
    val remoteId: String,
    val content: String,
    val createdAt: String,
    val lastEditedAt: String? = null,
    val title: String
)
