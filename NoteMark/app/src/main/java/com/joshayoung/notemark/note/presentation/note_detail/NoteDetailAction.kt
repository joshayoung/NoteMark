package com.joshayoung.notemark.note.presentation.note_detail

import com.joshayoung.notemark.note.presentation.note_list.NoteListAction


sealed interface NoteDetailAction {
    data object GoBack: NoteDetailAction
    data class GoToEdit(val id: Int?) : NoteDetailAction
}