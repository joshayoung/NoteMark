package com.joshayoung.notemark.note.presentation.note_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import com.joshayoung.notemark.core.design.theme.PlusIcon
import com.joshayoung.notemark.core.presentation.components.NoteMarkScaffold
import com.joshayoung.notemark.core.presentation.components.NoteMarkToolbar
import com.joshayoung.notemark.note.presentation.model.NoteUi
import org.koin.androidx.compose.koinViewModel


@Composable
fun NoteListScreenRoot(
    viewModel: NoteListViewModel = koinViewModel()
) {

    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()

    NoteListScreen(
       state = viewModel.state.collectAsStateWithLifecycle().value,
        isOffline = !isConnected,
       onAction = { action ->
            viewModel.onAction(action)
       }
    )
}

@Composable
fun NoteListScreen(
    state: NoteListState,
    isOffline: Boolean,
    onAction: (NoteListAction) -> Unit
) {
    NoteMarkScaffold(
        topAppBar = {
            NoteMarkToolbar(
                title = "Note Mark",
                hasBackButton = false,
                hasActions = true,
                isOffline = isOffline,
                userAbbreviation = state.userAbbreviation,
                navigateToSettings = {
                    onAction(NoteListAction.OnSettingsClick)
                }
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(size = 26.dp))
                    .size(70.dp)
                    .shadow(10.dp)
                    .clickable(onClick = {
                        onAction(NoteListAction.AddNoteClick)
                    }

                    )
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF58A1F8),
                                Color(0xFF5A4CF7),
                            ),
                            startY = 0f,
                            endY = 200f
                        )
                    )
                    .graphicsLayer {
                        shadowElevation = 10f
                        alpha = 0.8f
                    }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = PlusIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                        ),
                        center = Offset(0.5f, 0.5f),
                        radius = 9300f
                    )
                )
                .padding(innerPadding)
                .padding(10.dp)
                .fillMaxSize()
        ) {
            if (!state.hasItems) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .padding(top = 60.dp),
                    text = "You've got an empty board, let's place your first note on it!")
            }

            if (state.hasItems) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalItemSpacing = 10.dp,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    items(state.notes) { item ->
                        NoteItem(
                            item,
                            onAction = onAction,
                            onNavigateToEdit = {
                                onAction(NoteListAction.GoToDetail(item.id ?: 0))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoteItem(
    note: NoteUi,
    onAction: (NoteListAction)-> Unit,
    onNavigateToEdit: (id: Long) -> Unit
    ) {
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }

    if (showConfirmDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDeleteDialog = false },
            title = {
                Text("Delete Note?")
            },
            text = { Text(text = "Are you sure you want to delete this note? This action cannot be undone.")},
            confirmButton = {
                TextButton(onClick = {
                    showConfirmDeleteDialog = false
                    note.id?.let{
                        onAction(NoteListAction.OnDeleteClick(note))
                    }
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showConfirmDeleteDialog = false
                }) {
                    Text("Cancel")
                }
            }
       )
    }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(size = 20.dp))
            .background(Color.White.copy(alpha = 0.8f))
            .combinedClickable(
                onClick = {
                    if (note.id != null) {
                        onNavigateToEdit(note.id)
                    }
                },
                onLongClick = {
                    showConfirmDeleteDialog = true
                }
            )
            .padding(20.dp)
    ) {
        Text(text = note.date,
            modifier = Modifier
                ,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(text = note.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
            )
        Text(
            text = note.content,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoteListScreenPreview() {
    NoteMarkTheme {
        NoteListScreen(
            state = NoteListState(
                userAbbreviation = "NM",
                hasItems = true,
                notes = listOf(
                    NoteUi(
                        id = 1,
                        remoteId = "",
                        title = "My First Note",
                        content = "Augue non mauris ante viverra ut arcu sed ut lectus interdum morbi sed leo purus gravida non id mi augue.",
                        createdAt = "2025-07-26T16:16:05Z"
                    ),
                    NoteUi(
                        id = 2,
                        remoteId = "",
                        title = "Second Note",
                        content = "Augue non mauris ante viverra ut arcu sed ut lectus interdum morbi sed leo purus gravida non id mi augue.",
                        createdAt = "2025-07-26T16:16:05Z"
                    ),
                    NoteUi(
                        id = 3,
                        remoteId = "",
                        title = "Another Note With",
                        content = "Augue non mauris ante viverra ut arcu sed ut lectus interdum morbi sed leo purus gravida non id mi augue.",
                        createdAt = "2025-07-26T16:16:05Z"
                    )
                )
            ),
            isOffline = true,
            onAction = {}
        )
    }
}