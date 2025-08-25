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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshayoung.notemark.core.design.theme.CloseIcon
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import com.joshayoung.notemark.note.presentation.components.DashedDivider
import com.joshayoung.notemark.note.presentation.components.NoteMarkScaffold
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddNoteScreenRoot(viewModel: AddNoteViewModel = koinViewModel()) {
    AddNoteScreen(
        state = viewModel.state,
        onAction = { action ->
            viewModel.onAction(action)
        },
    )
}

@Composable
fun AddNoteScreen(
    state: AddNoteState,
    onAction: (AddNoteAction) -> Unit,
) {
    NoteMarkScaffold(
        topAppBar = {
            Row(
                modifier =
                    Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = CloseIcon,
                    contentDescription = null,
                    modifier =
                        Modifier
                            .size(26.dp)
                            .clickable {
                                onAction(AddNoteAction.NavigateBack)
                            },
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(innerPadding)
                    .fillMaxSize(),
        ) {
            NoteTextFieldSingleLine(state = state.noteTitle)
            NoteTextFieldMultiline(state = state.noteBody)
        }
    }
}

@Composable
fun NoteTextFieldSingleLine(state: TextFieldState) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column {
        BasicTextField(
            state = state,
            textStyle =
                TextStyle(
                    fontSize = 40.sp,
                    color = MaterialTheme.colorScheme.primary,
                ),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        isFocused = it.isFocused
                    }.focusRequester(focusRequester)
                    .padding(20.dp),
            decorator = { innerBox ->
                Box {
                    if (state.text.isEmpty() && !isFocused) {
                        Text(
                            text = "Note Title",
                            modifier = Modifier,
                            fontSize = 40.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    innerBox()
                }
            },
        )
    }
    DashedDivider(MaterialTheme.colorScheme.primary)
}

@Composable
fun NoteTextFieldMultiline(state: TextFieldState) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    Column {
        BasicTextField(
            state = state,
            textStyle =
                LocalTextStyle.current.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
            decorator = { innerBox ->
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier =
                            Modifier
                                .weight(1f),
                    ) {
                        if (state.text.isEmpty() && !isFocused) {
                            Text(text = "Note Body")
                        }
                        innerBox()
                    }
                }
            },
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                ),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        isFocused = it.isFocused
                    }.padding(20.dp)
                    .clip(RoundedCornerShape(4.dp)),
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
        )
    }
}