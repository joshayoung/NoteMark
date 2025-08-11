package com.joshayoung.notemark.note.presentation.note_detail


sealed interface NoteDetailAction {
    data object GoBack: NoteDetailAction
}