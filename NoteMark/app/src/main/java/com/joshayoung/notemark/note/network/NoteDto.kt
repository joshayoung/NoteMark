package com.joshayoung.notemark.note.network

import kotlinx.serialization.Serializable

@Serializable
data class NoteDto(
    val content: String,
    val createdAt: String,
    val id: String,
    val lastEditedAt: String? = null,
    val title: String
)
