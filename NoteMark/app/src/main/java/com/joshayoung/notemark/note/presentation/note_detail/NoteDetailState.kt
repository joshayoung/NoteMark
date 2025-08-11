package com.joshayoung.notemark.note.presentation.note_detail

data class NoteDetailState(
    val dataCreated: String = "",
    val lastEdited: String = "",
    val body: String = "",
    val viewMode: NoteViewMode? = null
)