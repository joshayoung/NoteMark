package com.joshayoung.notemark.note.presentation.add_note

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.notemark.core.design.theme.CloseIcon
import com.joshayoung.notemark.core.presentation.components.NoteMarkScaffold
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import com.joshayoung.notemark.core.presentation.components.NoteMarkTextField
import org.koin.androidx.compose.koinViewModel


@Composable
fun AddNoteScreenRoot(
    modifier: Modifier,
    viewModel: AddNoteViewModel = koinViewModel(),
    redirectBack: () -> Unit,
    navigateBack: () -> Unit,
) {
    AddNoteScreen(
        modifier = modifier,
        state = viewModel.state,
        onAction = { action ->
            when(action) {
                AddNoteAction.OnSaveClick -> {
                    redirectBack()
                }

                AddNoteAction.OnCloseClick -> TODO()
            }
            viewModel.onAction(action)
        },
        navigateBack = navigateBack
    )
}

@Composable
fun AddNoteScreen(
    modifier: Modifier,
    state: AddNoteState,
    onAction: (AddNoteAction)-> Unit,
    navigateBack: () -> Unit
) {
    var showNonSavePrompt by remember { mutableStateOf(false) }

    if (showNonSavePrompt && state.hasChangeInitialContent) {
        AlertDialog(
            onDismissRequest = {
                showNonSavePrompt = false
            },
            title = { Text("Discard Changes?") },
            text = { Text("You have unsaved changes. If you discard now, all changes will be lost.") },
            confirmButton = {
                Button(onClick = {
                    showNonSavePrompt = false
                    navigateBack()
                }) {
                    Text("Discard")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showNonSavePrompt = false
                }) {
                    Text("Keep Editing")
                }
            }
        )
    }

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
                Icon(imageVector = CloseIcon, contentDescription = null, modifier = Modifier
                    .clickable {
                        if (state.hasChangeInitialContent) {
                            showNonSavePrompt = true
                        } else {
                        navigateBack()
                        }
                    },
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
            modifier = modifier
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

    Column {
        BasicTextField(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    isFocused = it.isFocused
                }
                .padding(20.dp),
            decorator = { innerBox ->
                Box() {
                    if (state.text.isEmpty() && !isFocused) {
                        Text(text = "Note Title")
                    }
                    innerBox()
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
//        NoteMarkTextField(
//            state = state,
//            hint = "test",
//            helperText = "test",
//            label ="test",
//            keyboardType = KeyboardType.Text)
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
                        if (state.text.isEmpty() && !isFocused) {
                            Text(text = "Note Body")
                        }
                        innerBox()
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
            onAction = {},
            modifier = Modifier,
            navigateBack = {}
        )
    }
}