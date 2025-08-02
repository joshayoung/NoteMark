package com.joshayoung.notemark.note.presentation.note_landing.model


data class NoteUi(
    val id: String? = null,
    val title: String,
    val content: String,
    val createdAt: String,
    val lastEditedAt: String? = null
)