package com.joshayoung.notemark.presentation.add_note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joshayoung.notemark.ui.theme.NoteMarkTheme


@Composable
fun AddNoteScreenRoot() {
    AddNoteScreen()
}

@Composable
fun AddNoteScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add Note")
    }
}

@Composable
@Preview(showBackground = true)
fun AddNoteScreenPreview() {
    NoteMarkTheme {
        AddNoteScreen()
    }
}