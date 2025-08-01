package com.joshayoung.notemark.note.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val content: String,
    val createdAt: String,
    val id: String,
    val lastEditedAt: String? = null,
    val title: String
)
