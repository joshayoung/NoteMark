package com.joshayoung.notemark.note.presentation.note_landing

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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joshayoung.notemark.core.presentation.components.NoteMarkScaffold
import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.domain.models.NotesData
import com.joshayoung.notemark.core.design.theme.NoteMarkTheme
import com.joshayoung.notemark.core.design.theme.PlusIcon
import com.joshayoung.notemark.note.presentation.note_landing.model.NoteUi
import org.koin.androidx.compose.koinViewModel
import java.nio.file.WatchEvent
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
fun NoteLandingScreenRoot(
    viewModel: NoteLandingViewModel = koinViewModel(),
    onAddNoteClick: () -> Unit,
    onNavigateToEdit: (id: String) -> Unit
) {
    NoteLandingScreen(
       state = viewModel.state.collectAsStateWithLifecycle().value,
        onAction = { action ->
            viewModel.onAction(action)
        },
        onAddNoteClick = onAddNoteClick,
        onNavigateToEdit = onNavigateToEdit
    )
}

@Composable
fun NoteLandingScreen(
    state: NoteLandingState,
    onAction: (NoteLandingAction) -> Unit,
    onAddNoteClick: () -> Unit,
    onNavigateToEdit: (id: String) -> Unit
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
                Box(modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(size = 10.dp))
                    .background(brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.userAbbreviation, modifier = Modifier,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(size = 26.dp))
                    .size(70.dp)
                    .shadow(10.dp)
                    .clickable(onClick = onAddNoteClick)
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
                .background(Color.LightGray)
                .padding(innerPadding)
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Button(onClick = {
                onAction(NoteLandingAction.OnLogoutClick)
            }) {
                Text("Log Out")
            }
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
                            onNavigateToEdit = onNavigateToEdit
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
    onAction: (NoteLandingAction)-> Unit,
    onNavigateToEdit: (id: String) -> Unit
    ) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(size = 20.dp))
            .background(Color.White.copy(alpha = 0.8f))
            .padding(20.dp)
            .clickable {
                if (note.id != null) {
                    onNavigateToEdit(note.id)
                }
            }
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
        Button(onClick = {
            note.id?.let { onAction(NoteLandingAction.OnDeleteClick(it)) }
        }) {
            Text("Delete")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteLandingScreenPreview() {
    NoteMarkTheme {
        NoteLandingScreen(
            state = NoteLandingState(
                hasItems = true,
                notes = listOf(
                    NoteUi(
                        id = "1",
                        title = "My First Note",
                        content = "Augue non mauris ante viverra ut arcu sed ut lectus interdum morbi sed leo purus gravida non id mi augue.",
                        createdAt = "2025-07-26T16:16:05Z"
                    ),
                    NoteUi(
                        id = "2",
                        title = "Second Note",
                        content = "Augue non mauris ante viverra ut arcu sed ut lectus interdum morbi sed leo purus gravida non id mi augue.",
                        createdAt = "2025-07-26T16:16:05Z"
                    ),
                    NoteUi(
                        id = "3",
                        title = "Another Note With",
                        content = "Augue non mauris ante viverra ut arcu sed ut lectus interdum morbi sed leo purus gravida non id mi augue.",
                        createdAt = "2025-07-26T16:16:05Z"
                    )
                )
            ),
            onAction = {},
            onAddNoteClick = {},
            onNavigateToEdit = {}
        )
    }
}