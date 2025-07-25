package com.joshayoung.notemark.presentation.note_landing

import android.R
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.notemark.core.presentation.components.NoteMarkScaffold
import com.joshayoung.notemark.ui.theme.NoteMarkTheme
import com.joshayoung.notemark.ui.theme.PlusIcon
import org.koin.androidx.compose.koinViewModel

@Composable
fun NoteLandingScreenRoot(
    viewModel: NoteLandingViewModel = koinViewModel(),
    onLogoutClick: () -> Unit,
    onAddNoteClick: () -> Unit
) {
    NoteLandingScreen(
        onAction = { action ->
            when(action) {
                NoteLandingAction.OnLogoutClick -> {
                    onLogoutClick()
                }
            }
            viewModel.onAction(action)
        },
        onAddNoteClick = onAddNoteClick
    )
}

@Composable
fun NoteLandingScreen(
    onAction: (NoteLandingAction) -> Unit,
    onAddNoteClick: () -> Unit
) {
    NoteMarkScaffold(
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(size = 26.dp))
                    .size(70.dp)
                    .shadow(10.dp)
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
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .padding(top = 60.dp),
                text = "You've got an empty board, let's place your first note on it!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteLandingScreenPreview() {
    NoteMarkTheme {
        NoteLandingScreen(
            onAction = {},
            onAddNoteClick = {}
        )
    }
}