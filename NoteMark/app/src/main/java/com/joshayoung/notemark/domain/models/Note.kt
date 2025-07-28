package com.joshayoung.notemark.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val content: String,
    val createdAt: String,
    val id: String,
    val lastEditedAt: String? = null,
    val title: String
)
