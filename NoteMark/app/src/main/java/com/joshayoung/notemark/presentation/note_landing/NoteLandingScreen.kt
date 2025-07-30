package com.joshayoung.notemark.presentation.note_landing

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.joshayoung.notemark.domain.models.Note
import com.joshayoung.notemark.domain.models.Notes
import com.joshayoung.notemark.ui.theme.NoteMarkTheme
import com.joshayoung.notemark.ui.theme.PlusIcon
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
fun NoteLandingScreenRoot(
    viewModel: NoteLandingViewModel = koinViewModel(),
    onAddNoteClick: () -> Unit
) {
    NoteLandingScreen(
       state = viewModel.state.collectAsStateWithLifecycle().value,
        onAction = { action ->
            viewModel.onAction(action)
        },
        onAddNoteClick = onAddNoteClick
    )
}

@Composable
fun NoteLandingScreen(
    state: NoteLandingState,
    onAction: (NoteLandingAction) -> Unit,
    onAddNoteClick: () -> Unit
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
                    Text(text = "pl".uppercase(), modifier = Modifier,
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
                    items(state.notes.notes) { item ->
                        NoteItem(item)
                    }
                }
            }
        }
    }
}

@Composable
fun NoteItem(note: Note) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(size = 20.dp))
            .background(Color.White.copy(alpha = 0.8f))
            .padding(20.dp)
    ) {
        val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm:ss")

        val dateTime = OffsetDateTime.parse(note.createdAt, inputFormatter)
        val formatted = dateTime.format(outputFormatter)

        Text(text = formatted)
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
fun NoteLandingScreenPreview() {
    NoteMarkTheme {
        NoteLandingScreen(
            state = NoteLandingState(
                hasItems = true,
                notes = Notes(
                    notes = listOf(
                        Note(
                            id = "1",
                            title = "My First Note",
                            content = "Augue non mauris ante viverra ut arcu sed ut lectus interdum morbi sed leo purus gravida non id mi augue.",
                            createdAt = "2025-07-26T16:16:05Z"
                        ),
                        Note(
                            id = "2",
                            title = "Second Note",
                            content = "Augue non mauris ante viverra ut arcu sed ut lectus interdum morbi sed leo purus gravida non id mi augue.",
                            createdAt = "2025-07-26T16:16:05Z"
                        ),
                        Note(
                            id = "3",
                            title = "Another Note With",
                            content = "Augue non mauris ante viverra ut arcu sed ut lectus interdum morbi sed leo purus gravida non id mi augue.",
                            createdAt = "2025-07-26T16:16:05Z"
                        )
                    ),
                    total = 3
                )
            ),
            onAction = {},
            onAddNoteClick = {}
        )
    }
}