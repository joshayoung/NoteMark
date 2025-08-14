package com.joshayoung.notemark.note.presentation.note_list

import com.joshayoung.notemark.note.presentation.model.NoteUi

data class NoteListState(
    var notes: List<NoteUi>,
    var hasItems: Boolean = false,
    var userAbbreviation: String = ""
)
