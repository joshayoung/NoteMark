package com.joshayoung.notemark.presentation.note_landing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joshayoung.notemark.ui.theme.NoteMarkTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun NoteLandingScreenRoot(
    viewModel: NoteLandingViewModel = koinViewModel(),
    onLogoutClick: () -> Unit
) {
    NoteLandingScreen(
        onAction = { action ->
            when(action) {
                NoteLandingAction.OnLogoutClick -> {
                    onLogoutClick()
                }
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun NoteLandingScreen(
    onAction: (NoteLandingAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text("Add a note...")
        Button(onClick = {
            onAction(NoteLandingAction.OnLogoutClick)
        }) {
            Text("Logout")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteLandingScreenPrieview() {
    NoteMarkTheme {
        NoteLandingScreen(onAction = {})
    }
}