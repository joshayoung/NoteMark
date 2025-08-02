package com.joshayoung.notemark.note.domain.models

data class Note(
    val id: String? = null,
    val content: String,
    val createdAt: String,
    val lastEditedAt: String? = null,
    val title: String
)
