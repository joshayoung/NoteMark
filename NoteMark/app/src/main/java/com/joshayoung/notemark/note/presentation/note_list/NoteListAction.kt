package com.joshayoung.notemark.note.presentation.note_list

import com.joshayoung.notemark.note.presentation.note_list.model.NoteUi


sealed interface NoteListAction {
    data class OnDeleteClick(val noteUi: NoteUi): NoteListAction
}