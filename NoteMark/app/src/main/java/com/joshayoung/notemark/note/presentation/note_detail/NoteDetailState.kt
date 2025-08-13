package com.joshayoung.notemark.note.presentation.note_detail

data class NoteDetailState(
    val noteId: Int? = null,
    val dataCreated: String = "",
    val lastEdited: String = "",
    val body: String = "",
    val title: String = "",
    val viewMode: NoteViewMode? = null,
    val hasChanged: Boolean = false
)