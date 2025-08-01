package com.joshayoung.notemark.note.presentation.add_note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.notemark.core.presentation.components.NoteMarkScaffold
import com.joshayoung.notemark.ui.theme.NoteMarkTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun AddNoteScreenRoot(
    viewModel: AddNoteViewModel = koinViewModel(),
    redirectBack: () -> Unit
) {
    AddNoteScreen(
        state = viewModel.state,
        onAction = { action ->
            when(action) {
                AddNoteAction.OnSaveClick -> {
                    redirectBack()
                }
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun AddNoteScreen(
    state: AddNoteState,
    onAction: (AddNoteAction)-> Unit
) {
    NoteMarkScaffold(
        topAppBar = {
            Row(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 20.dp)
                ,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("NoteMark", modifier = Modifier,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = {
                        onAction(AddNoteAction.OnSaveClick)
                    }
                ) {
                    Text("Save Note")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            NoteTextFieldSingleLine(state = state.noteTitle)
            NoteTextFieldMultiline(state = state.noteBody)
        }
    }
}

@Composable
fun NoteTextFieldSingleLine(
    state: TextFieldState,
) {

    var isFocused by remember {
        mutableStateOf(false)
    }

    Column() {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    isFocused = it.isFocused
                }
                .padding(20.dp)
            ,state = state,
            decorator = { innerBox ->

                Box() {
                    innerBox()
                    if (state.text.isEmpty() && !isFocused) {
                        Text(text = "Note Title")
                    }
                }
            }
        )
    }
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun NoteTextFieldMultiline(
    state: TextFieldState,
) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    Column() {
        BasicTextField(
            state = state,
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            decorator = { innerBox ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        innerBox()

                        if (state.text.isEmpty() && !isFocused) {
                            Text(text = "Note Body")
                        }
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    isFocused = it.isFocused
                }
                .clip(RoundedCornerShape(4.dp))
                .padding(12.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun AddNoteScreenPreview() {
    NoteMarkTheme {
        AddNoteScreen(
            state = AddNoteState(),
            onAction = {}
        )
    }
}