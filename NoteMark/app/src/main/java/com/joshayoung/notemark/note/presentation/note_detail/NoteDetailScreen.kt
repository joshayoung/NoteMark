package com.joshayoung.notemark.note.presentation.note_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel


@Composable
fun NoteDetailScreenRoot(
    viewModel: NoteDetailViewModel = koinViewModel(),
) {
    NoteDetailScreen(
        state = viewModel.state
    )
}

@Composable
fun NoteDetailScreen(
    state: NoteDetailState
) {
    Column(
    ) {
        Text(text = "test")
    }
}

@Preview
@Composable
fun NoteDetailScreenPreview() {

}