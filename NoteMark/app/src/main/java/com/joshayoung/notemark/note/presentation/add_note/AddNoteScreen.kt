package com.joshayoung.notemark.note.presentation.add_note

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshayoung.notemark.core.design.theme.CloseIcon
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import com.joshayoung.notemark.core.presentation.components.DashedDivider
import com.joshayoung.notemark.core.presentation.components.NoteMarkScaffold
import org.koin.androidx.compose.koinViewModel


@Composable
fun AddNoteScreenRoot(
    viewModel: AddNoteViewModel = koinViewModel()
) {
    AddNoteScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                AddNoteAction.OnCloseClick -> TODO()
                else -> {
                    viewModel.onAction(action)
                }
            }
        }
    )
}

@Composable
fun AddNoteScreen(
    state: AddNoteState,
    onAction: (AddNoteAction)-> Unit
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
                    onAction(AddNoteAction.NavigateBack)
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
                    .size(26.dp)
                    .clickable {
                        if (state.hasChangeInitialContent) {
                            showNonSavePrompt = true
                        } else {
                            onAction(AddNoteAction.NavigateBack)
                        }
                    },
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Save Note".uppercase()
                    , modifier = Modifier
                        .padding(end = 10.dp)
                    .clickable {
                        onAction(AddNoteAction.OnSaveClick)
                    },
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall
                )
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
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit)  {
        focusRequester.requestFocus()
    }

    Column {
        BasicTextField(
            state = state,
            textStyle = TextStyle(
                fontSize = 40.sp,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    isFocused = it.isFocused
                }
                .focusRequester(focusRequester)
                .padding(20.dp),
            decorator = { innerBox ->
                Box() {
                    if (state.text.isEmpty() && !isFocused) {
                        Text(text = "Note Title", modifier = Modifier
                            ,fontSize = 40.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    innerBox()
                }
            }
        )
    }
    DashedDivider(MaterialTheme.colorScheme.primary)
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
                .padding(20.dp)
                .clip(RoundedCornerShape(4.dp))
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