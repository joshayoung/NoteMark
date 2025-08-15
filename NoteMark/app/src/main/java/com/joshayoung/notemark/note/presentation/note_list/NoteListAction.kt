package com.joshayoung.notemark.note.presentation.note_list

import com.joshayoung.notemark.note.presentation.model.NoteUi


sealed interface NoteListAction {
    data class OnDeleteClick(val noteUi: NoteUi): NoteListAction
    data object AddNoteClick: NoteListAction
    data class GoToDetail(val id: Long) : NoteListAction
    data object OnSettingsClick: NoteListAction
}