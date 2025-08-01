package com.joshayoung.notemark.note.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Notes(
    val notes: List<Note>,
    val total: Int = 0
)