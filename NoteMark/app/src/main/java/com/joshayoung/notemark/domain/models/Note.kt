package com.joshayoung.notemark.domain.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.Date
import java.util.UUID

@Serializable
data class Note(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val lastEditedAt: String? = null
)