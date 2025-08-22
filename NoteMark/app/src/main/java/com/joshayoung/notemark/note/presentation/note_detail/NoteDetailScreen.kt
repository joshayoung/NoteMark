package com.joshayoung.notemark.note.presentation.note_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joshayoung.notemark.core.design.theme.BookIcon
import com.joshayoung.notemark.core.design.theme.EditIcon
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import com.joshayoung.notemark.core.presentation.components.NoteMarkScaffold
import com.joshayoung.notemark.core.presentation.components.NoteMarkToolbar
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun NoteDetailScreenRoot(viewModel: NoteDetailViewModel = koinViewModel()) {
    NoteDetailScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onAction = { action ->
            viewModel.onAction(action)
        },
    )
}

@Composable
fun NoteDetailScreen(
    state: NoteDetailState,
    onAction: (NoteDetailAction) -> Unit,
) {
    var inReaderMode by remember { mutableStateOf(false) }
    var tappedToCloseReaderMode by remember { mutableStateOf(false) }
    var timer by remember { mutableStateOf(0) }

    LaunchedEffect(tappedToCloseReaderMode) {
        if (!tappedToCloseReaderMode) {
            return@LaunchedEffect
        }

        while (timer < 5) {
            delay(1000)
            timer += 1
        }

        if (timer == 5) {
            inReaderMode = true
            tappedToCloseReaderMode = false
            timer = 0
        }
    }
    NoteMarkScaffold(
        topAppBar = {
            AnimatedVisibility(
                visible = !inReaderMode,
                enter = fadeIn(),
                exit =
                    fadeOut(
                        animationSpec = tween(durationMillis = 1000),
                    ),
            ) {
                NoteMarkToolbar(
                    title = "All Notes".uppercase(),
                    hasBackButton = true,
                    goBack = {
                        onAction(NoteDetailAction.GoBack)
                    },
                )
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !inReaderMode,
                enter = fadeIn(),
                exit =
                    fadeOut(
                        animationSpec = tween(durationMillis = 1000),
                    ),
            ) {
                Row(
                    modifier =
                        Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(color = MaterialTheme.colorScheme.surface)
                            .shadow(100.dp),
                ) {
                    IconButton(onClick = {
                        onAction(NoteDetailAction.GoToEdit(state.noteId))
                    }) {
                        Icon(
                            imageVector = EditIcon,
                            contentDescription = null,
                            modifier =
                                Modifier
                                    .size(20.dp),
                        )
                    }

                    val readerColor =
                        if (inReaderMode) {
                            MaterialTheme.colorScheme.surface
                        } else {
                            Color.Transparent
                        }

                    IconButton(
                        modifier =
                            Modifier
                                .background(readerColor),
                        onClick = {
                            inReaderMode = !inReaderMode
                        },
                    ) {
                        Icon(
                            imageVector = BookIcon,
                            contentDescription = null,
                            modifier =
                                Modifier
                                    .size(20.dp),
                        )
                    }
                }
            }
        },
        fabPosition = FabPosition.Center,
    ) { innerPadding ->
        val contentPadding: Dp = 20.dp
        Column(
            modifier =
                Modifier
                    .clickable(onClick = {
                        if (inReaderMode) {
                            inReaderMode = false
                            tappedToCloseReaderMode = true
                        }
                    })
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            Text(
                text = state.title,
                modifier =
                    Modifier
                        .padding(contentPadding),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
            )
            HorizontalDivider()
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(contentPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(
                        text = "Date created",
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = state.dataCreated,
                    )
                }
                Column {
                    Text(
                        text = "Last edited",
                        fontWeight = FontWeight.Bold,
                    )
                    Text(text = state.lastEdited)
                }
            }
            HorizontalDivider()
            Text(
                text = state.body,
                modifier =
                    Modifier
                        .padding(contentPadding),
            )
        }
    }
}

@Preview
@Composable
fun NoteDetailScreenPreview() {
    NoteMarkTheme {
        NoteDetailScreen(
            state =
                NoteDetailState(
                    noteId = 1,
                    title = "Note Title",
                    dataCreated = "26 Sep 2024, 18:54",
                    lastEdited = "Just now",
                    body = "Lorem ipsum dolor sit olor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur s",
                    viewMode = NoteViewMode.DISPLAY,
                ),
            onAction = {},
        )
    }
}