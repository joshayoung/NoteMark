package com.joshayoung.notemark.note.presentation.note_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import com.joshayoung.notemark.core.design.theme.BackIcon
import com.joshayoung.notemark.core.design.theme.BookIcon
import com.joshayoung.notemark.core.design.theme.EditIcon
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import com.joshayoung.notemark.core.presentation.components.NoteMarkScaffold
import com.joshayoung.notemark.core.presentation.components.NoteMarkToolbar
import org.koin.androidx.compose.koinViewModel


@Composable
fun NoteDetailScreenRoot(
    viewModel: NoteDetailViewModel = koinViewModel(),
) {
    NoteDetailScreen(
        state = viewModel.state,
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@Composable
fun NoteDetailScreen(
    state: NoteDetailState,
    onAction: (NoteDetailAction) -> Unit
) {
    Column {
        NoteMarkScaffold(
            topAppBar = {
                NoteMarkToolbar(
                    title = "All Notes".uppercase(),
                    hasBackButton = true,
                    goBack = {
                        onAction(NoteDetailAction.GoBack)
                    }
                )
            },
            floatingActionButton = {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(color = MaterialTheme.colorScheme.surface)
                        .shadow(100.dp)

                ) {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = EditIcon,
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = BookIcon,
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }
                }
            },
            fabPosition = FabPosition.Center,
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(20.dp)
            ) {
                Text(text = state.title)
                Text(text = "Date created")
                Text(text = "Last edited")
                Text(text = state.lastEdited)
                Text(text = state.body)
            }
        }
    }
}

@Preview
@Composable
fun NoteDetailScreenPreview() {
    NoteMarkTheme {
        NoteDetailScreen(
            state = NoteDetailState(
                title = "Note Title",
                dataCreated = "2025-08-11T20:58:34.490Z",
                lastEdited = "2025-08-11T20:58:53.992Z",
                body = "Lorem ipsum dolor sit olor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur s",
                viewMode = NoteViewMode.DISPLAY
            ),
            onAction = {}
        )
    }
}