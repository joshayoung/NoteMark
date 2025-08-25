package com.joshayoung.notemark.note.presentation.note_detail

import com.joshayoung.notemark.note.presentation.model.NoteViewMode

data class NoteDetailState(
    val noteId: Long? = null,
    val dataCreated: String = "",
    val lastEdited: String = "",
    val body: String = "",
    val title: String = "",
    val viewMode: NoteViewMode? = null,
    val hasChanged: Boolean = false,
)