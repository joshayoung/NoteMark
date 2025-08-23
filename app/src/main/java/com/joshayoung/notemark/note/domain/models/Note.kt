package com.joshayoung.notemark.note.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Long? = null,
    val remoteId: String?,
    val content: String?,
    val createdAt: String?,
    val lastEditedAt: String? = null,
    val title: String,
)
